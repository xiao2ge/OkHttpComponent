package com.xiao2ge.library.okhttpcomponent.net;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * NetRequest
 *
 * @author LR
 * @date 2019/5/10 14:45
 */
public class NetRequest {

    private static final int REQUEST_METHOD_GET = 0;
    private static final int REQUEST_METHOD_POST = 1;

    private OkHttpClient mClient;
    private long id;

    public NetRequest(OkHttpClient mClient, long id) {
        this.mClient = mClient;
        this.id = id;
    }

    private int method = REQUEST_METHOD_GET;
    private String url;
    private HashMap<String, String> paramMap = new HashMap<>();
    private HashMap<String, String> headerMap = new HashMap<>();
    private HashMap<String, String> fileMap = new HashMap<>();

    public NetRequest get() {
        this.method = REQUEST_METHOD_GET;
        return this;
    }

    public NetRequest post() {
        this.method = REQUEST_METHOD_POST;
        return this;
    }

    public NetRequest url(String url) {
        this.url = url;
        return this;
    }

    public NetRequest addParam(String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            paramMap.put(key, value);
        }
        return this;
    }

    public NetRequest addHeader(String key, String value) {
        if (!TextUtils.isEmpty(key)) {
            headerMap.put(key, value);
        }
        return this;
    }

    public NetRequest addFile(String key, String filePath) {
        if (!TextUtils.isEmpty(key)) {
            fileMap.put(key, filePath);
        }
        return this;
    }

    public void enqueue(final NetListener listener) {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                        StringBuilder sb = new StringBuilder();
                        sb.append(" \n======================\t\t").append(id).append("\tRequest\tStart\t\t").append("======================\n");
                        Request.Builder builder = new Request.Builder();
                        if (method == REQUEST_METHOD_POST) {
                            builder.url(url);
                            FormBody.Builder bodyBuilder = new FormBody.Builder();
                            for (String key : paramMap.keySet()) {
                                bodyBuilder.add(key, paramMap.get(key));
                            }
                            builder.post(bodyBuilder.build());
                            sb.append("||\t").append("POST\t").append(url).append("\n");
                            sb.append("||\t").append("Params\t").append(paramMap.toString()).append("\n");
                        } else {
                            if (paramMap.size() > 0) {
                                url += "?";
                                for (String key : paramMap.keySet()) {
                                    url += (key + "=" + paramMap.get(key) + "&");
                                }
                                url = url.substring(0, url.length() - 1);
                            }
                            builder.url(url);
                            builder.get();
                            sb.append("||\t").append("GET\t").append(url).append("\n");
                        }
                        for (String key : headerMap.keySet()) {
                            builder.addHeader(key, headerMap.get(key));
                        }
                        sb.append("||\t").append("Headers\t").append(headerMap.toString()).append("\n");
                        sb.append("======================\t\t").append(id).append("\tRequest\tEnd\t\t").append("======================\n");
                        NetLog.i(sb.toString());
                        final Request request = builder.build();
                        Call call = mClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                emitter.onError(e);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String result = response.body().string();
                                if (response.isSuccessful()) {
                                    emitter.onNext(result);
                                    emitter.onComplete();
                                } else {
                                    emitter.onError(new RuntimeException(result));
                                }
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        NetLog.i(" \n>>>>>>\tonSubscribe");
                        if (listener != null) {
                            listener.onRequestStart();
                        }
                    }

                    @Override
                    public void onNext(String json) {
                        NetLog.i(" \n>>>>>>\tResponse\t" + json);
                        if (listener != null) {
                            listener.onResponse(json);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        NetLog.e(" \n>>>>>>\tError\t" + e.getMessage(), e);
                        if (listener != null) {
                            listener.onRequestEnd();
                            listener.onError(e.getMessage(), e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        NetLog.i(" \n>>>>>>\tComplete\t");
                        if (listener != null) {
                            listener.onRequestEnd();
                        }
                    }
                });
    }

    public void uploadAllFile(final NetListener listener) {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM);

                        // 添加其他参数
                        for (String key : paramMap.keySet()) {
                            requestBodyBuilder.addFormDataPart(key, paramMap.get(key));
                        }

                        // 添加文件
                        for (String key : fileMap.keySet()) {
                            File file = new File(fileMap.get(key));
                            requestBodyBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(FileUtils.getMimeType(file)), file));
                        }

                        Request.Builder builder = new Request.Builder();
                        builder.url(url);
                        builder.post(requestBodyBuilder.build());
                        for (String key : headerMap.keySet()) {
                            builder.addHeader(key, headerMap.get(key));
                        }
                        final Request request = builder.build();
                        Call call = mClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                emitter.onError(e);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String result = response.body().string();
                                if (response.isSuccessful()) {
                                    emitter.onNext(result);
                                    emitter.onComplete();
                                } else {
                                    emitter.onError(new RuntimeException(result));
                                }
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        NetLog.i(" \n>>>>>>\tonSubscribe");
                        if (listener != null) {
                            listener.onRequestStart();
                        }
                    }

                    @Override
                    public void onNext(String json) {
                        NetLog.i(" \n>>>>>>\tResponse\t" + json);
                        if (listener != null) {
                            listener.onResponse(json);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        NetLog.e(" \n>>>>>>\tError\t" + e.getMessage(), e);
                        if (listener != null) {
                            listener.onRequestEnd();
                            listener.onError(e.getMessage(), e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        NetLog.i(" \n>>>>>>\tComplete\t");
                        if (listener != null) {
                            listener.onRequestEnd();
                        }
                    }
                });

    }

    public void uploadFile(final NetProgressListener listener) {
        Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(final ObservableEmitter<String> emitter) throws Exception {

                        Request.Builder requestBuilder = new Request.Builder();

                        MultipartBody.Builder builder = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM);

                        for (String key : paramMap.keySet()) {
                            builder.addFormDataPart(key, paramMap.get(key));
                        }

                        String name = "";
                        String filePath = "";
                        for (String key : fileMap.keySet()) {
                            name = key;
                            filePath = fileMap.get(key);
                            break;
                        }
                        File file = new File(filePath);
                        if (TextUtils.isEmpty(name)) {
                            emitter.onError(new RuntimeException("文件参数名不能为空"));
                        } else if (!file.exists()) {
                            emitter.onError(new RuntimeException("文件不存在"));
                        } else {
                            builder.addFormDataPart(name, file.getName(), RequestBody.create(MediaType.parse(FileUtils.getMimeType(file)), file));
                            FileProgressRequestBody body = new FileProgressRequestBody(builder.build(), null);

                            for (String key : headerMap.keySet()) {
                                requestBuilder.addHeader(key, headerMap.get(key));
                            }

                            Request request = requestBuilder.url(url)
                                    .post(body)
                                    .build();

                            StringBuilder sb = new StringBuilder();
                            sb.append(" \n======================\t\t").append(id).append("\tRequest\tStart\t\t").append("======================\n");
                            sb.append("||\t").append("POST\t").append(url).append("\n");
                            sb.append("||\t").append("Params\t").append(paramMap.toString()).append("\n");
                            sb.append("||\t").append("File\t").append(name).append("\n").append(filePath).append("\n");
                            sb.append("||\t").append("Headers\t").append(headerMap.toString()).append("\n");
                            sb.append("======================\t\t").append(id).append("\tRequest\tEnd\t\t").append("======================\n");
                            NetLog.i(sb.toString());
                            Call call = mClient.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    emitter.onError(e);
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String result = response.body().string();
                                    if (response.isSuccessful()) {
                                        emitter.onNext(result);
                                        emitter.onComplete();
                                    } else {
                                        emitter.onError(new RuntimeException(result));
                                    }
                                }
                            });
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        NetLog.i(" \n>>>>>>\tonSubscribe");
                        if (listener != null) {
                            listener.onRequestStart();
                        }
                    }

                    @Override
                    public void onNext(String json) {
                        NetLog.i(" \n>>>>>>\tResponse\t" + json);
                        if (listener != null) {
                            listener.onResponse(json);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        NetLog.e(" \n>>>>>>\tError\t" + e.getMessage(), e);
                        if (listener != null) {
                            listener.onRequestEnd();
                            listener.onError(e.getMessage(), e);
                        }
                    }

                    @Override
                    public void onComplete() {
                        NetLog.i(" \n>>>>>>\tComplete\t");
                        if (listener != null) {
                            listener.onRequestEnd();
                        }
                    }
                });
    }
}
