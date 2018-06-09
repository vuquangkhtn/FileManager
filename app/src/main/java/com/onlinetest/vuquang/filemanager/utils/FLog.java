package com.onlinetest.vuquang.filemanager.utils;

import android.os.Environment;
import android.util.Log;

import com.onlinetest.vuquang.filemanager.BuildConfig;

import java.io.File;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class FLog {
    private static final String TAG = "FileManager";
    private static final String desFilePath = Environment.getExternalStorageDirectory()
            + File.separator + "log_filemanager.txt";

    public static void show(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        } else {
            logToFile(desFilePath, message);
        }
    }

    private static void logToFile(String desFilePath, String message) {

        FileHelper.append(desFilePath, message);
    }
}
