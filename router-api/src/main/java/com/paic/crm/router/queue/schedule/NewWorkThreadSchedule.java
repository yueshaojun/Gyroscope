package com.paic.crm.router.queue.schedule;


import com.paic.crm.router.queue.threadfactory.WorkThreadFactory;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yueshaojun988 on 2017/10/24.
 */

public class NewWorkThreadSchedule implements Schedule{
    private static final String NEW_WORK_PREFIX = "newWork";
    private ThreadPoolExecutor executor;
    private static int TIMEOUT = 60*1000;
    private NewWorkThreadSchedule(ThreadFactory factory){
        executor = new ThreadPoolExecutor(1,Integer.MAX_VALUE,TIMEOUT, TimeUnit.MILLISECONDS,new SynchronousQueue<Runnable>(),factory);
    }

    public static NewWorkThreadSchedule getInstance(){
        return Holder.instance;
    }

    @Override
    public void schedule(Runnable runnable) {
        executor.submit(runnable);
    }

    private static class Holder{
        private static NewWorkThreadSchedule instance = new NewWorkThreadSchedule(new WorkThreadFactory(NEW_WORK_PREFIX));
    }
}
