package com.paic.crm.router.queue.looper;

/**
 *
 * @author yueshaojun988
 * @date 2017/10/24
 */

public final class Looper {
    MessageQueue messageQueue;
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();
    private static Looper sMainLooper;
    private Thread mThread;

    /**
     * 绑定Looper和线程、MessageQueue，不能直接new Looper（）；
     * 需要在使用Handler之前调用，否则会抛出异常。
     */
    public static void prepare(){
        if(sThreadLocal.get()!=null){
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper());
    }
    private Looper(){
        mThread = Thread.currentThread();
        messageQueue = new MessageQueue();
    }

    /**
     * 类似于prepare（），但是这个方法是用来绑定主线程的。
     */
    public static void prepareMainLooper() {
        prepare();
        synchronized (Looper.class) {
            if (sMainLooper != null) {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
            sMainLooper = sThreadLocal.get();
        }
    }

    /**
     * 获取主线程的looper
     * @return
     */
    public static Looper mainLooper(){
        return sMainLooper;
    }

    /**
     * 开始循环取消息，必须要在prepare之后
     */
    public static void loop(){
        for(;;) {
            checkAccess();
            Message message =  myLooper().messageQueue.next();

            //没有退出循环，除非应用退出
            if (message == null) {
                continue;
            }
            message.target.dispatchMessage(message);
        }
    }

    /**
     * 获取当前线程的looper
     * @return
     */
    public static Looper myLooper(){
        return sThreadLocal.get();
    }

    private static void checkAccess() {
        if(sThreadLocal.get()==null){
            throw new RuntimeException("no looper!");
        }
    }
}
