package com.onlinetest.vuquang.filemanager.data.model.action;

import com.onlinetest.vuquang.filemanager.utils.FLog;
import com.onlinetest.vuquang.filemanager.utils.FileHelper;

import java.text.MessageFormat;

/**
 * Created by VuQuang on 6/10/2018.
 */

public class CreateFileAction implements FileAction {
    private String path;

    public CreateFileAction(String path) {
        this.path = path;
    }

    @Override
    public boolean execute() {
        if(FileHelper.createFile(path)) {
            logMessage();
            return true;
        }
        return false;
    }

    @Override
    public void logMessage() {
        FLog.show(MessageFormat.format("Create File {0}", path));
    }

    @Override
    public boolean undo() {
        return FileHelper.removeFile(path);
    }
}
