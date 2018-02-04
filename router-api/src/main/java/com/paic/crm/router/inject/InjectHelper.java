package com.paic.crm.router.inject;

import java.lang.reflect.Constructor;

/**
 * 注入辅助类
 * @author yueshaojun988
 * @date 2017/12/26
 */

public class InjectHelper {
    /**
     * 注入Receiver
     * @param host
     */
    public static void injectReceiver(Object host) {
        String classFullName = host.getClass().getName() + "$$RouterReceiverInjector";
        invokeConstructor(classFullName,host);
    }
    /**
     * 注入field
     * @param host
     */
    public static void injectField(Object host) {
        String classFullName = host.getClass().getName() + "$$RouterFieldInjector";
        invokeConstructor(classFullName,host);
    }
    private static void invokeConstructor(String classFullName,Object host){
        try {
            Class proxy = Class.forName(classFullName);
            Constructor constructor = proxy.getConstructor(host.getClass());
            constructor.newInstance(host);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
