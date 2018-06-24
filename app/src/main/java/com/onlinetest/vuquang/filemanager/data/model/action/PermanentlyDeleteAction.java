package com.onlinetest.vuquang.filemanager.data.model.action;

import com.onlinetest.vuquang.filemanager.log.AppLog;
import com.onlinetest.vuquang.filemanager.utils.FileHelper;

import java.text.MessageFormat;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class PermanentlyDeleteAction implements FileAction {
    private String path;

    public PermanentlyDeleteAction(String path) {
        this.path = path;
    }

    @Override
    public boolean execute() {
        if(FileHelper.removeFile(path)) {
            logMessage();
            return true;
        }
        return false;
    }

    @Override
    public void logMessage() {
        AppLog.show(MessageFormat.format("Permanently Delete {0}", path));

    }

    @Override
    public boolean undo() {
        return false;
    }
}
