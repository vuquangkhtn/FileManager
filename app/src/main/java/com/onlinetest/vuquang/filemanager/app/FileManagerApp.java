package com.onlinetest.vuquang.filemanager.app;

import android.app.Application;
import android.util.Log;

/**
 * Created by VuQuang on 6/10/2018.
 */

public class FileManagerApp extends Application{
    private static FileManagerApp instance;

    private String curPath;

    @Override
    public void onCreate() {
        super.onCreate();
        curPath = "";
        instance = this;
    }

    public static FileManagerApp getApp() {
        return instance;
    }

    public String getCurPath() {
        return curPath;
    }

    public void setCurPath(String curPath) {
        this.curPath = curPath;
    }
}
