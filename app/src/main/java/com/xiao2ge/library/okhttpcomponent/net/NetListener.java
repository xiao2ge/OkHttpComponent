package com.xiao2ge.library.okhttpcomponent.net;

/**
 * NetListener
 *
 * @author LR
 * @date 2019/5/10 14:41
 */
public interface NetListener {

    void onRequestStart();

    void onRequestEnd();

    void onResponse(String json);

    void onError(String msg, Throwable e);
}
