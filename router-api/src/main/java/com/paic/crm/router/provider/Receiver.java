package com.paic.crm.router.provider;

import com.paic.crm.router.router.RouterIntent;

/**
 * Created by yueshaojun988 on 2018/1/10.
 */

public class Receiver {
    private RouterIntent intent;

    public RouterIntent getIntent() {
        return intent;
    }

    public void setIntent(RouterIntent intent) {
        this.intent = intent;
    }
}
