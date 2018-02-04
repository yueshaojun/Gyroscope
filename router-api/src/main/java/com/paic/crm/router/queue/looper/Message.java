package com.paic.crm.router.queue.looper;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author yueshaojun988
 * @date 2017/10/24
 */

public class Message implements Delayed{
    public int what;
    public Object obj;
    long when;
    Handler target;
    Runnable callback;
    private AtomicLong sequence = new AtomicLong(0);
    private long sequenceNumber ;

    /**
     * 生成Message的方法，绑定handler
     * @param target
     * @return
     */
    public static Message obtain(Handler target){
        return obtain(target,null);
    }

    /**
     * 生成Message的方法，绑定handler和Runnable
     * @param target
     * @param callback
     * @return
     */
    public static Message obtain(Handler target ,Runnable callback){

        Message msg = new Message();
        msg.target = target;
        msg.callback = callback;
        return msg;
    }
    public Message(){
        sequenceNumber = sequence.getAndIncrement();
        when = now();
    }

    /**
     * 发送到指定的Handler
     */
    public void sendToTarget(){
        target.sendMessageDelay(this,0);
    }

    /**
     * 延时发送到指定的handler
     * @param delay
     */
    public void sendToTargetDelay(long delay){
        checkAccess();
        when = System.currentTimeMillis() + delay;
        target.sendMessageDelay(this,delay);
    }
    private void checkAccess(){
        if(target == null){
            throw new RuntimeException("target must not be null !");
        }
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
        return timeUnit.convert(when - now(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed other) {
        if(this == other){
            return 0;
        }
        if(other instanceof Message){
            Message o = (Message) other;
            long diff = when - o.when;
            if(diff<0){
                return -1;
            }
            if(diff>0){
                return 1;
            }
            if(sequenceNumber < o.sequenceNumber){
                return -1;
            }
        }
        long d = (getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }

    private long now(){
        return System.currentTimeMillis();
    }

}
