package com.paic.crm.router.compiler;

import com.google.auto.service.AutoService;
import com.paic.crm.router.annotation.IntentField;
import com.paic.crm.router.annotation.Receive;
import com.paic.crm.router.compiler.parser.IntentFiledParser;
import com.paic.crm.router.compiler.parser.ReceiverParser;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;


/**
 * 注解处理器
 * @author yueshaojun988
 * @date 2017/12/26
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class RouterProcessor extends AbstractProcessor {

    Filer filer;
    Elements elementUtil;
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        ReceiverParser.createInjectFile(roundEnvironment,elementUtil,filer);
        IntentFiledParser.createInjectFile(roundEnvironment,elementUtil,filer);
        return false;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        elementUtil = processingEnvironment.getElementUtils();
    }
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(IntentField.class.getCanonicalName());
        types.add(Receive.class.getCanonicalName());
        return types;
    }
}
