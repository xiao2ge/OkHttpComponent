package com.xiao2ge.library.okhttpcomponent.net;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 * FileProgressRequestBody
 *
 * @author LR
 * @date 2019/5/10 15:23
 */
public class FileProgressRequestBody extends RequestBody {

    public static final int SEGMENT_SIZE = 2 * 1024; // okio.Segment.SIZE

    private MultipartBody multipartBody;
    private NetProgressListener listener;
    private long currentLength;

    public FileProgressRequestBody(MultipartBody multipartBody, NetProgressListener listener) {
        this.multipartBody = multipartBody;
        this.listener = listener;
    }

    @Override
    public long contentLength() throws IOException {
        return multipartBody.contentLength();
    }

    @Override
    public MediaType contentType() {
        return multipartBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        //这里需要另一个代理类来获取写入的长度
        ForwardingSink forwardingSink = new ForwardingSink(sink) {
            final long totalLength = contentLength();

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                //这里可以获取到写入的长度
                currentLength += byteCount;
                //回调进度
                if (listener != null) {
                    listener.onProgress(currentLength, totalLength);
                }
                NetLog.i(" \n>>>>>>\tonProgress\tcurrentLength=" + currentLength + "\ttotalLength" + totalLength);
                super.write(source, byteCount);
            }
        };

        //转一下
        BufferedSink bufferedSink = Okio.buffer(forwardingSink);
        //写数据
        multipartBody.writeTo(bufferedSink);
        //刷新一下数据
        bufferedSink.flush();
    }


}
