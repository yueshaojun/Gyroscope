package com.paic.crm.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.paic.crm.router.router.Router;

public class Main2Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        String str = getIntent().getExtras().getString("signature");
        Log.d("Main2Activity",str);
        TextView textView = (TextView) findViewById(R.id.textyy);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Testuu testuu = new Router().create(Testuu.class);
                testuu.getParam("yueshaojun",988);
            }
        });
    }
}
