package com.paic.crm.router.provider;


/**
 *
 * @author yueshaojun988
 * @date 2017/12/26
 */

public interface ReceiverListener {
    /**
     * 接受数据监听
     * @param methodName
     * @param params
     */
    void onReceive(String methodName, Object... params);
}
