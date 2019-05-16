package com.cekong.panran.okhttpcomponent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cekong.panran.okhttpcomponent.net.NetListener;
import com.cekong.panran.okhttpcomponent.net.NetManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetManager.getInstance().newRequest()
                        .url("http://www.panran.vip/wenbiao/contacts/search")
                        .addParam("keyword", "泰安")
                        .post()
                        .enqueue(new NetListener() {
                            @Override
                            public void onRequestStart() {
                                toast("onRequestStart");
                            }

                            @Override
                            public void onRequestEnd() {
                                toast("onRequestEnd");
                            }

                            @Override
                            public void onResponse(String json) {
                                toast("onResponse  " + json);
                            }

                            @Override
                            public void onError(String msg, Throwable e) {
                                toast("onError  " + msg);
                            }
                        });
            }
        });

    }

    private void toast(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        Log.e("TAG", s);
    }
}
