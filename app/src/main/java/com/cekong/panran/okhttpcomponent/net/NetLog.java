package com.cekong.panran.okhttpcomponent.net;

import android.util.Log;

/**
 * NetLog
 *
 * @author LR
 * @date 2019/5/16 14:27
 */
class NetLog {

    static void i(String tag, String msg) {
        if (NetManager.DEBUG) {
            Log.i(tag, msg);
        }
    }

    static void e(String tag, String msg, Throwable e) {
        if (NetManager.DEBUG) {
            Log.e(tag, msg, e);
        }
    }
}
