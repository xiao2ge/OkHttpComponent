package com.cekong.panran.okhttpcomponent.net;

/**
 * RequestListener
 *
 * @author LR
 * @date 2019/5/10 14:41
 */
public interface RequestListener {

    void onRequestStart();

    void onRequestEnd();

    void onResponse(String json);

    void onError(String msg, Throwable e);
}
