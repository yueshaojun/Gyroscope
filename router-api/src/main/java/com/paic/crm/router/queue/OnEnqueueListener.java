package com.paic.crm.router.queue;

/**
 * Created by yueshaojun988 on 2017/12/13.
 */

public interface OnEnqueueListener {
    /**
     * 入队列
     * @param taskId 任务ID
     */
    void onEnqueue(long taskId);
}
