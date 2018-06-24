package com.onlinetest.vuquang.filemanager.app;

import android.app.Application;
import android.util.Log;

import com.onlinetest.vuquang.filemanager.main.MainPresenter;
import com.onlinetest.vuquang.filemanager.main.sort.AbstractSort;
import com.onlinetest.vuquang.filemanager.utils.FLog;

/**
 * Created by VuQuang on 6/10/2018.
 */

public class FileManagerApp extends Application{
    private static FileManagerApp instance;

    private String curPath;

    private FileManagerApp() {};

    @Override
    public void onCreate() {
        super.onCreate();
        curPath = "";
        instance = this;
        //Todo: config log service
        FLog.setLogType(FLog.LOG_FILE);
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
