package com.paic.crm.router.queue.threadfactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author yueshaojun988
 * @date 2017/10/24
 */

public class WorkThreadFactory extends AtomicLong implements ThreadFactory{
    private String prefix;
    public WorkThreadFactory(String prefix){
        this.prefix = prefix;
    }
    @Override
    public Thread newThread(Runnable runnable) {
        Thread t = new Thread(runnable,prefix+"-"+incrementAndGet());
        t.setDaemon(true);
        return t;
    }
}
