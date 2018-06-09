package com.onlinetest.vuquang.filemanager.utils;

import android.util.Log;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class FLog {
    private static final String TAG = "OpenedFileManager";

    public static final int LOG_CONSOLE = 0;
    public static final int LOG_FILE = 1;

    private static int logType = 0;

    public static void show(String message) {
        switch (logType) {
            case LOG_CONSOLE: {
                Log.d(TAG, message);
                break;
            }
            case LOG_FILE: {
                logToFile(LocalPathUtils.DES_LOG_FILE, message);
                break;
            }
            default: {
                Log.d(TAG,"Wrong log type");
            }
        }
    }

    public static void setLogType(int type) {
        logType = type;
    }

    private static void logToFile(String desFilePath, String message) {
        FileHelper.appendStrToFile(desFilePath, message);
    }
}
