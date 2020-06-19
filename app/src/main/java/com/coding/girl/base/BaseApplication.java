package com.coding.girl.base;

import android.app.Application;
import android.content.Context;

/**
 * Demo 的 Application 入口。
 * Created by HDL on 19/1/12.
 */
public class BaseApplication extends Application {

    private Context context;

    public Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
