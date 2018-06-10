package com.onlinetest.vuquang.filemanager.data.model.action;

import com.onlinetest.vuquang.filemanager.utils.FLog;
import com.onlinetest.vuquang.filemanager.utils.FileHelper;

import java.text.MessageFormat;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class CreateFolderAction implements FileAction {
    private String path;

    public CreateFolderAction(String path) {
        this.path = path;
    }

    @Override
    public boolean execute() {
        if(FileHelper.createDirectory(path)) {
            logMessage();
            return true;
        }
        return false;
    }

    @Override
    public void logMessage() {
        FLog.show(MessageFormat.format("Create {0}", path));
    }

    @Override
    public boolean undo() {
        return FileHelper.removeFile(path);
    }
}
