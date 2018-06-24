package com.onlinetest.vuquang.filemanager.log;

import com.onlinetest.vuquang.filemanager.app.LocalPathUtils;
import com.onlinetest.vuquang.filemanager.utils.FileHelper;

/**
 * Created by VuQuang on 6/24/2018.
 */

public class FileLog implements AbstractLog{
    @Override
    public void log(String message) {
        FileHelper.appendStrToFile(LocalPathUtils.DES_LOG_FILE, message);
    }
}
