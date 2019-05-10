package com.cekong.panran.okhttpcomponent.net;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class NetManager {

    private static NetManager mInstance;

    private OkHttpClient mHttpClient;
    private Gson mGson;
    private long index;

    private static int outTome = 30;

    public static void init(int outTome) {
        NetManager.outTome = outTome;
    }

    private NetManager() {
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(outTome, TimeUnit.SECONDS)
                .build();
        mGson = new Gson();
    }

    public static NetManager getInstance() {
        if (mInstance == null) {
            mInstance = new NetManager();
        }
        return mInstance;
    }

    public HttpRequest newRequest() {
        index++;
        return new HttpRequest(mHttpClient, mGson, index);
    }

}
