package com.onlinetest.vuquang.filemanager.log;

import android.util.Log;

/**
 * Created by VuQuang on 6/24/2018.
 */

public class ConsoleLog implements AbstractLog {
    @Override
    public void log(String message) {
        Log.d(TAG, message);
    }
}
