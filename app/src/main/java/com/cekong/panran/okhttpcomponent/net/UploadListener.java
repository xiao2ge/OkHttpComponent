package com.cekong.panran.okhttpcomponent.net;

/**
 * UploadListener
 *
 * @author LR
 * @date 2019/5/10 14:50
 */
public interface UploadListener {

    void onUploadStart();

    void onProgress(int progress);

    void onUploadEnd();

    void onError(String msg, Throwable e);
}
