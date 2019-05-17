package com.xiao2ge.library.okhttpcomponent.net;

import android.util.Log;

/**
 * NetLog
 *
 * @author LR
 * @date 2019/5/16 14:27
 */
class NetLog {

    private static final String TAG = "OKHTTP";

    static void i(String msg) {
        if (NetManager.DEBUG) {
            Log.i(TAG, msg);
        }
    }

    static void e(String msg, Throwable e) {
        if (NetManager.DEBUG) {
            Log.e(TAG, msg, e);
        }
    }
}
