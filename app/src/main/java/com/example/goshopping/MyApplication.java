package com.example.goshopping;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * date:2018/11/20
 * author:别的小朋友(别的小朋友)
 * function:
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderConfiguration build = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(build);
    }
}
