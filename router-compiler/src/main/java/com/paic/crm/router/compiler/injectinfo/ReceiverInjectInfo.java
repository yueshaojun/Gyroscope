package com.paic.crm.router.compiler.injectinfo;

import javax.lang.model.element.ExecutableElement;

/**
 *
 * @author yueshaojun988
 * @date 2017/12/26
 */

public class ReceiverInjectInfo {
    private String className;
    private ExecutableElement executableElement;
    private String targetMethodName ;
    public void setClassName(String className) {
        this.className = className;
    }

    public void setExecutableElement(ExecutableElement executableElement) {
        this.executableElement = executableElement;
    }

    public ExecutableElement getExecutableElement() {
        return executableElement;
    }

    public String getClassName() {
        return className;
    }

    public String getTargetMethodName() {
        return targetMethodName;
    }

    public void setTargetMethodName(String targetMethodName) {
        this.targetMethodName = targetMethodName;
    }
}
