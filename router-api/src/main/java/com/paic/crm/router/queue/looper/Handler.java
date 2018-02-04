package com.paic.crm.router.queue.looper;

import com.paic.crm.router.queue.OnEnqueueListener;

/**
 *
 * @author yueshaojun988
 * @date 2017/10/24
 */

public class Handler {
    private Looper mLooper;
    protected void dispatchMessage(Message msg){
        if(msg.callback!=null){
            msg.callback.run();
        }
    }
    public Handler(Looper looper){
        mLooper = looper;
    }
    public Handler(){
        mLooper = Looper.myLooper();
    }

    /**
     * post 一个方法
     * @param runnable
     */
    public void post(Runnable runnable){
        checkAccess();
        Message msg = Message.obtain(this,runnable);
        mLooper.messageQueue.enqueue(msg);
    }

    /**
     * 延迟 DelayTime post 一个方法
     * @param runnable
     * @param delayTime
     */
    public void postDelay(Runnable runnable,long delayTime){
        checkAccess();
        Message msg = Message.obtain(this,runnable);
        msg.when = (now()+delayTime);
        mLooper.messageQueue.enqueueDelay(msg,delayTime);
    }

    /**
     * 消息延迟入队列
     * @param message
     * @param delayTime
     */
    public void sendMessageDelay(Message message,long delayTime){
        checkAccess();
        mLooper.messageQueue.enqueueDelay(message,delayTime);
    }

    private void checkAccess(){
        if(mLooper == null){
            throw new RuntimeException("you must call Looper.prepare() first!");
        }
    }

    private long now(){
        return System.currentTimeMillis();
    }

    /**
     * 消息入队列的通知
     * @param enqueueListener
     */
    public void addOnEnqueueListener(OnEnqueueListener enqueueListener){
        mLooper.messageQueue.addOnEnqueueListener(enqueueListener);
    }

}
