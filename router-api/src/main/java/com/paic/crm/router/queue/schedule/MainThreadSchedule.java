package com.paic.crm.router.queue.schedule;


import android.os.Handler;
import android.os.Looper;


/**
 * Created by yueshaojun988 on 2017/10/24.
 */

public class MainThreadSchedule implements Schedule {
    private Handler mainThreadHandler;
    @Override
    public void schedule(Runnable runnable) {
        mainThreadHandler.post(runnable);
    }
    private MainThreadSchedule(){
        mainThreadHandler = new Handler(Looper.getMainLooper());
    }

    public static MainThreadSchedule getInstance(){
        return Holder.instance;
    }

    private static class Holder{
        private static MainThreadSchedule instance = new MainThreadSchedule();
    }

}
