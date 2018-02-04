package com.paic.crm.router.compiler.parser;

import com.paic.crm.router.annotation.IntentField;
import com.paic.crm.router.compiler.injectinfo.IntentFieldInjectInfo;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * intent 字段解析类
 * @author yueshaojun988
 * @date 2017/12/28
 */

public class IntentFiledParser {
    private static HashMap<String,List<IntentFieldInjectInfo>> intentFieldInjectInfoMap = new HashMap<>();
    private static HashMap<String,TypeElement> typeElements = new HashMap<>();
    public static void createInjectFile(RoundEnvironment roundEnvironment, Elements elementUtil, Filer filer){
        parse(roundEnvironment);
        writeToFieldInjectFile(elementUtil, filer);
    }
    private static void parse(RoundEnvironment roundEnvironment){
        Set<?extends Element> elements = roundEnvironment.getElementsAnnotatedWith(IntentField.class);
        for(Element element : elements){
            ElementKind elementKind = element.getKind();
            if(ElementKind.FIELD == elementKind){
                VariableElement variableElement = (VariableElement) element;
                IntentField field = variableElement.getAnnotation(IntentField.class);
                String fieldName = field.name();
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                String className = typeElement.getSimpleName().toString();
                List<IntentFieldInjectInfo> injectInfos = intentFieldInjectInfoMap.get(className);
                if(injectInfos==null){
                    injectInfos = new ArrayList<>();
                    intentFieldInjectInfoMap.put(className,injectInfos);
                    typeElements.put(className,typeElement);
                }
                IntentFieldInjectInfo injectInfo = new IntentFieldInjectInfo();
                injectInfo.setClassName(className);
                injectInfo.setVariableElement(variableElement);
                injectInfo.setFieldName(fieldName);
                injectInfos.add(injectInfo);
            }
        }
    }
    private static void writeToFieldInjectFile(Elements elementUtil, Filer filer){
        for(String className : intentFieldInjectInfoMap.keySet()){
            TypeElement typeElement = typeElements.get(className);
            List<IntentFieldInjectInfo> injectInfos = intentFieldInjectInfoMap.get(className);
            MethodSpec.Builder constructor =
                    MethodSpec.
                    constructorBuilder().
                            addModifiers(Modifier.PUBLIC).
                    addParameter(ParameterSpec.
                            builder(TypeName.get(typeElement.asType()),"host",Modifier.FINAL).build());
            StringBuilder formatBuilder = new StringBuilder();
            Object[] objects = new Object[injectInfos.size()*3];
            int i = 0;
            for(IntentFieldInjectInfo injectInfo : injectInfos){
                formatBuilder.append("host.$N = ($T)dataBundle.get($S);\n");
                objects[i]=injectInfo.getVariableElement().getSimpleName().toString();
                objects[i+1]=injectInfo.getVariableElement().asType();
                objects[i+2]=injectInfo.getFieldName();
                i = i+3;
            }
            constructor.addStatement(
                    "android.os.Bundle dataBundle = new android.os.Bundle();\n" +
                    "if(host instanceof android.app.Activity ){\n" +
                    "            dataBundle = ((android.app.Activity)host).getIntent().getExtras();\n" +
                    "}\n" +
                    formatBuilder.toString(),objects);
            TypeSpec typeSpec = TypeSpec.classBuilder(typeElement.getSimpleName()+"$$RouterFieldInjector").
                    addMethod(constructor.build()).
                    build();
            String packageName = elementUtil.getPackageOf(typeElement).getQualifiedName().toString();
            try {
                JavaFile.builder(packageName,typeSpec).build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
