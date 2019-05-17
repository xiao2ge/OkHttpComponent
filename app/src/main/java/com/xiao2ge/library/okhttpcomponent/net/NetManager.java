package com.xiao2ge.library.okhttpcomponent.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class NetManager {

    private static NetManager mInstance;

    private OkHttpClient mHttpClient;
    private long index;
    protected static boolean DEBUG = false;

    private static int outTome = 30;

    public static void init(int outTome, boolean debug) {
        NetManager.outTome = outTome;
        DEBUG = debug;
    }

    private NetManager() {
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(outTome, TimeUnit.SECONDS)
                .build();
    }

    public static NetManager getInstance() {
        if (mInstance == null) {
            mInstance = new NetManager();
        }
        return mInstance;
    }

    public NetRequest newRequest() {
        index++;
        return new NetRequest(mHttpClient, index);
    }

}
