package com.paic.crm.router.router;


import android.app.Application;

import com.paic.crm.router.queue.looper.Handler;

import java.io.InputStream;

/**
 *
 * @author yueshaojun988
 * @date 2017/12/21
 */

public abstract class RouterManager {
    private static final String VERSION_CODE = "1.0.0";
    public RouterManager(){
    }
    public static RouterManagerInstance getInstance(){
        return RouterManagerInstance.manager;
    }

    /**
     * 设置全局application
     * @param application
     * @return
     */
    public abstract RouterManager setApplication(Application application);

    /**
     * 设置队列的handler,包私有
     * @param looperHandler
     * @return
     */
    abstract RouterManager setLooperHandler(Handler looperHandler);

    /**
     * 获取队列的Handler
     * @return
     */
    public abstract Handler getLooperHandler();

    /**
     * 初始化
     */
    public abstract void init();

    /**
     * 设置配置文件流
     * @param is
     * @return
     */
    public abstract RouterManager setConfig(InputStream is);

    /**
     * 获取配置流
     * @return
     */
    abstract InputStream getConfig();
}
