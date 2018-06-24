package com.onlinetest.vuquang.filemanager.log;

import android.util.Log;

import com.onlinetest.vuquang.filemanager.app.LocalPathUtils;
import com.onlinetest.vuquang.filemanager.utils.FileHelper;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class AppLog {
    public static AbstractLog log;

    static {
        log = new ConsoleLog();
    }

    public static void show(String message) {
        if(log != null) {
            log.log(message);
        }
    }

    public static void setLog(AbstractLog abstractLog) {
        log = abstractLog;
    }
}
