package com.onlinetest.vuquang.filemanager.data.model.modify;

import com.onlinetest.vuquang.filemanager.data.model.FileAction;
import com.onlinetest.vuquang.filemanager.utils.FLog;
import com.onlinetest.vuquang.filemanager.utils.FileHelper;
import com.onlinetest.vuquang.filemanager.utils.LocalPathUtils;

import java.text.MessageFormat;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class PermanentlyDeleteAction implements ModifyAction {
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
        FLog.show(MessageFormat.format("Permanently Delete {1}", path));

    }

    @Override
    public boolean undo() {
        return false;
    }
}
