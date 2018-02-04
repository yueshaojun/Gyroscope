package com.paic.crm.router.compiler.parser;

import com.paic.crm.router.annotation.Receive;
import com.paic.crm.router.compiler.injectinfo.ReceiverInjectInfo;
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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by yueshaojun988 on 2017/12/28.
 */

public class ReceiverParser {
    private static HashMap<String,List<ReceiverInjectInfo>> injectInfoMap = new HashMap<>();
    private static HashMap<String,TypeElement> typeElements = new HashMap<>();
    public static void createInjectFile(RoundEnvironment roundEnvironment, Elements elementUtil, Filer filer){
        parse(roundEnvironment);
        writeToReceiverFile(elementUtil, filer);
    }
    private static void parse(RoundEnvironment roundEnvironment){
        injectInfoMap.clear();
        typeElements.clear();

        Set<? extends Element> receiverSet =
                roundEnvironment.getElementsAnnotatedWith(Receive.class);
        for (Element e : receiverSet) {
            ElementKind elementKind = e.getKind();
            if(elementKind == ElementKind.METHOD){
                ExecutableElement executableElement = (ExecutableElement) e;
                Receive receive = executableElement.getAnnotation(Receive.class);
                String targetMethodName = receive.methodName();
                TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
                String classPath = typeElement.getQualifiedName().toString();
                List<ReceiverInjectInfo> receiverInjectInfos = injectInfoMap.get(classPath);
                if(receiverInjectInfos == null){
                    receiverInjectInfos = new ArrayList<>();
                    injectInfoMap.put(classPath, receiverInjectInfos);
                    typeElements.put(classPath,typeElement);
                }
                ReceiverInjectInfo receiverInjectInfo = new ReceiverInjectInfo();
                receiverInjectInfo.setClassName(classPath);
                receiverInjectInfo.setExecutableElement(executableElement);
                receiverInjectInfo.setTargetMethodName(targetMethodName);
                receiverInjectInfos.add(receiverInjectInfo);
            }
        }
    }
    private static void writeToReceiverFile(Elements elementUtil,Filer filer) {
        for(String classPath : injectInfoMap.keySet()) {
            TypeElement typeElement = typeElements.get(classPath);
            List<ReceiverInjectInfo> receiverInjectInfoList = injectInfoMap.get(classPath);
            MethodSpec.Builder constructor = MethodSpec.
                    constructorBuilder().
                    addModifiers(Modifier.PUBLIC).
                    addParameter(ParameterSpec.builder(TypeName.get(typeElement.asType()),"host",Modifier.FINAL).
                            build());
            for(ReceiverInjectInfo i : receiverInjectInfoList){
                constructor.addStatement(
                        "com.paic.crm.router.router.RouterContainer.addReceiverListener(new com.paic.crm.router.provider.ReceiverListener() {" +
                        "            @Override" +
                        "            public void onReceive(String methodName, Object... params) {" +
                        "            android.util.Log.i(\"onReceive\",\"onReceive\");"+
                        "            if($S.equals(methodName)) {"+
                        "                host.$N(params);" +
                        "                }"+
                        "            }" +
                        "        });",i.getTargetMethodName(),i.getExecutableElement().getSimpleName());
            }
            TypeSpec typeSpec = TypeSpec.classBuilder(typeElement.getSimpleName()+"$$RouterReceiverInjector").
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
