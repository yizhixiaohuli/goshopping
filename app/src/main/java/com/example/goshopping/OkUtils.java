package com.example.goshopping;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * date:2018/11/20
 * author:别的小朋友(别的小朋友)
 * function:
 */
public class OkUtils {
    private static volatile OkUtils sUtils;
    private final Handler mHandler;
    private final OkHttpClient mOkHttpClient;

    public OkUtils() {
        mHandler = new Handler(Looper.getMainLooper());
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }
    public static OkUtils getInstance(){
        if (sUtils == null) {
            synchronized (OkUtils.class){
                if (sUtils==null){
                    sUtils=new OkUtils();
                }
            }
        }
        return sUtils;
    }
    public void doGet(String Url, final OkHttpCallBack httpCallBack){
        final Request request = new Request.Builder()
                .get()
                .url(Url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (httpCallBack!=null){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpCallBack.failed(e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response!=null&&response.isSuccessful()){
                    final String s = response.body().string();
                    if (httpCallBack!=null){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                httpCallBack.success(s);
                            }

                        });
                }
                }
            }
        });
    }
    public void doPost(String Url, Map<String,String> map, final OkHttpCallBack httpCallBack){

        FormBody.Builder builder = new FormBody.Builder();
        if (map!=null){
            for (String key:map.keySet()) {
                builder.add(key,map.get(key));
            }
        }


        FormBody body = builder.build();
        Request request = new Request.Builder()
                .post(body)
                .url(Url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (httpCallBack!=null){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpCallBack.failed(e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response!=null&&response.isSuccessful()){
                    final String s = response.body().string();
                    if (httpCallBack!=null){
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                httpCallBack.success(s);
                            }

                        });
                    }
                }
            }
        });
    }
    public interface OkHttpCallBack{
        void success(String s);
        void failed(Exception e);
    }
}
