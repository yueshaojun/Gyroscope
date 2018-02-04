package com.paic.crm.myapplication;

import android.os.Bundle;
import android.util.Log;

import com.paic.crm.router.annotation.IntentField;
import com.paic.crm.router.inject.InjectHelper;


public class Main3Activity extends BaseActivity {
    @IntentField(name = "signature")
    String a;
    @IntentField(name = "timestamp")
    int b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        InjectHelper.injectField(this);
        Log.d("Main3Activity","a="+a+"|b="+b);
    }
}
