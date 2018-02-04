package com.paic.crm.router.queue;

import android.content.Context;

import com.paic.crm.router.router.Dispatcher;
import com.paic.crm.router.uri.UriString;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author yueshaojun988
 * @date 2017/12/12
 */

public class Task implements Runnable{
    private AtomicLong taskId = new AtomicLong();
    private UriString uriString;
    private Context taskContext;
    public Task(Context context,UriString uri){
        taskId.getAndIncrement();
        uriString = uri;
        taskContext = context;
    }
    @Override
    public void run() {
        Dispatcher.dispatch(taskContext,uriString);
    }

    public long getTaskId() {
        return taskId.get();
    }
}
