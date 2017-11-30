package com.comcare.comcare_user;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.comcare.comcare_user.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class CustomFont extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initFont();

    }
    private void initFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/supermarket.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
