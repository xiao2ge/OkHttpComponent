package com.xiao2ge.library.okhttpcomponent.net;

/**
 * NetProgressListener
 *
 * @author LR
 * @date 2019/5/10 14:50
 */
public interface NetProgressListener extends NetListener {

    void onProgress(long currentLength, long totalLength);
}
