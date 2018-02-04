package com.paic.crm.router.queue.schedule;



import com.paic.crm.router.queue.threadfactory.WorkThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by yueshaojun988 on 2017/10/24.
 */

public class IOThreadSchedule implements Schedule{
    private static final String PREFIX = "IOThread";
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private IOThreadSchedule(ThreadFactory factory){
        scheduledThreadPoolExecutor =
                new ScheduledThreadPoolExecutor(1,factory);
    }

    public static IOThreadSchedule getInstance(){
        return Holder.instance;
    }

    @Override
    public void schedule(Runnable runnable) {
        schedule(runnable,0,TimeUnit.MILLISECONDS);
    }

    private static class Holder{
        private static IOThreadSchedule instance =
                new IOThreadSchedule(new WorkThreadFactory(PREFIX));
    }
    public void schedule(Runnable runnable, long l, TimeUnit timeUnit){
       scheduledThreadPoolExecutor.schedule(runnable, l, timeUnit);
    }
}
