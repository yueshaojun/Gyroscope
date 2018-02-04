package com.paic.crm.router.router;

import android.app.Application;
import android.util.Log;

import com.paic.crm.router.queue.looper.Handler;
import com.paic.crm.router.queue.looper.Looper;
import com.paic.crm.router.queue.schedule.NewWorkThreadSchedule;

import java.io.InputStream;

/**
 *
 * @author yueshaojun988
 * @date 2017/12/21
 */

public final class RouterManagerInstance extends RouterManager {
    static RouterManagerInstance manager = new RouterManagerInstance();
    private Handler handler;
    private Application application;
    private InputStream config;
    private RouterManagerInstance() {
        super();
    }

    @Override
    public RouterManager setApplication(Application application) {
        this.application = application;
        return this;
    }

    @Override
    RouterManager setLooperHandler(Handler looperHandler) {
        this.handler = looperHandler;
        return this;
    }

    @Override
    public Handler getLooperHandler() {
        return handler;
    }

    public Application getApplication(){
        return application;
    }

    @Override
    public void init(){
        Log.d("RouterManager","init");
        NewWorkThreadSchedule.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                RouterManager.getInstance().setLooperHandler(new Handler());
                RouterContainer.parse(application);
                Looper.loop();
            }
        });
//        RouterService.start(application);
    }

    @Override
    public RouterManager setConfig(InputStream is) {
        config = is;
        return this;
    }

    @Override
    InputStream getConfig() {
        return config;
    }


}
