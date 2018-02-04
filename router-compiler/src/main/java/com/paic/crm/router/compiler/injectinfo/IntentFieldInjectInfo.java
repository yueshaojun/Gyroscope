package com.paic.crm.router.compiler.injectinfo;

import javax.lang.model.element.VariableElement;

/**
 * intent 字段注入信息类
 * @author yueshaojun988
 * @date 2017/12/28
 */

public class IntentFieldInjectInfo {
    /**
     * 所在的类名
     */
    private String className;
    /**
     * 字段
     */
    private VariableElement variableElement;
    private String fieldName;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public VariableElement getVariableElement() {
        return variableElement;
    }

    public void setVariableElement(VariableElement variableElement) {
        this.variableElement = variableElement;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
