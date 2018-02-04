package com.paic.crm.router.provider;

import com.paic.crm.router.uri.Action;
import com.paic.crm.router.uri.Block;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yueshaojun988
 * @date 2017/12/17
 */

public class Provider {
    public String name ;
    public String path ;
    private List<ReceiverListener> receiverListeners = new ArrayList<>();
    public void addReceiverListener(ReceiverListener listener){
        receiverListeners.add(listener);
    }
    public void removeReceiverListener(ReceiverListener listener){
        receiverListeners.remove(listener);
    }
    public Block block;
    public void deliverToListener(Method method, Object[] args) {
        for (ReceiverListener l : receiverListeners){
            l.onReceive(method.getName(),args);
        }
    }
}
