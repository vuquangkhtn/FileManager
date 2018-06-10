package com.onlinetest.vuquang.filemanager.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class LocalPathUtils {
    private static final String LOG_FILE_NAME = "log_filemanager.txt";
    private static final String RECYCLE_BIN_NAME = ".recyclebin";
    public static final String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static final String DES_LOG_FILE = EXTERNAL_STORAGE + File.separator + LOG_FILE_NAME;

    public static final String RECYCLE_BIN_DIR = EXTERNAL_STORAGE + File.separator + RECYCLE_BIN_NAME;
}
