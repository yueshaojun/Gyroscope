package com.paic.crm.myapplication;

import android.app.Application;
import android.content.Context;

import com.paic.crm.router.router.RouterManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.IOException;


/**
 *
 * @author yueshaojun988
 * @date 2017/12/12
 */

public class AppContext extends Application{
    private RefWatcher watcher;
    @Override
    public void onCreate() {
        super.onCreate();
        RouterManager.getInstance().setApplication(this).init();
        watcher = LeakCanary.install(this);
    }
    public static RefWatcher getWatcher(Context context){
        return ((AppContext)context.getApplicationContext()).watcher;
    }
}
