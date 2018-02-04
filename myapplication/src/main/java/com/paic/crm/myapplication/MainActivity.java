package com.paic.crm.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.paic.crm.router.annotation.Receive;
import com.paic.crm.router.inject.InjectHelper;
import com.paic.crm.router.router.Router;

/**
 * @author yueshaojun988
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private String hh;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG,"onClick");
            Testuu testuu = new Router().create(Testuu.class);
            testuu.getParam("oooo",9);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setOnClickListener(this);
        InjectHelper.injectReceiver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Receive(methodName = "onNewMessage")
    public void oo(Object...params){
        Log.d(TAG,"oo"+params[0]);
    }
    @Receive(methodName = "onNewMessage")
    public void dd(Object...params){
        Log.d(TAG,"dd"+params[0]);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this.getApplicationContext(),Main2Activity.class);
        startActivity(intent);
    }
}
