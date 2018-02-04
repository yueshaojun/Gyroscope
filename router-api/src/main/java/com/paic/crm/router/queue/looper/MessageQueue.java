package com.paic.crm.router.queue.looper;

import com.paic.crm.router.queue.OnEnqueueListener;
import com.paic.crm.router.queue.Task;

import java.util.concurrent.DelayQueue;


/**
 *
 * @author yueshaojun988
 * @date 2017/10/24
 * 消息队列类
 */

class MessageQueue {

    private DelayQueue<Message> delayQueue = new DelayQueue<>();
    private OnEnqueueListener enqueueListener;
    MessageQueue(){}


    /**
     * 取出消息
     * @return
     */
    Message next(){
        Message msg = null;
        try {
             msg = delayQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return msg;

    }

    /**
     * 消息入队列
     * @param message
     */
    void enqueue(Message message){

        enqueueDelay(message,0);
        System.out.println("enqueue  = " + message.obj);
    }
    /**
     * 消息入队列
     * @param message
     * @param delayTime 延迟时间
     */
    void enqueueDelay(Message message, long delayTime){
        if(message == null){
            throw new RuntimeException("you cannot enqueue a null message!");
        }
        long now = now();
        message.when = delayTime + now ;
        if(delayQueue.offer(message)&&enqueueListener!=null){
            enqueueListener.onEnqueue(((Task)message.callback).getTaskId());
        }

    }
    void addOnEnqueueListener(OnEnqueueListener enqueueListener){
        this.enqueueListener = enqueueListener;
    }
    private long now (){
        return System.currentTimeMillis();
    }
}
