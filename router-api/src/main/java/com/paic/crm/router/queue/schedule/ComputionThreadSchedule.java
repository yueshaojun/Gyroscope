package com.paic.crm.router.queue.schedule;


import com.paic.crm.router.queue.threadfactory.WorkThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yueshaojun988 on 2017/11/1.
 */

public class ComputionThreadSchedule implements Schedule {
    private static final String COMPUTION_PREFIX = "COMPUTION";
    private ThreadPoolExecutor executor;
    private static int nCpu = Runtime.getRuntime().availableProcessors();
    private ComputionThreadSchedule(ThreadFactory factory){
        executor = new ThreadPoolExecutor(nCpu+1,nCpu+1,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(),factory);
    }
    private static class Holder{
        private static ComputionThreadSchedule instance = new ComputionThreadSchedule(new WorkThreadFactory(COMPUTION_PREFIX));
    }
    public static ComputionThreadSchedule getInstance(){
        return Holder.instance;
    }
    @Override
    public void schedule(Runnable runnable) {
        executor.submit(runnable);
    }
}
