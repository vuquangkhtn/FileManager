package com.onlinetest.vuquang.filemanager.data.model.modify;

import com.onlinetest.vuquang.filemanager.utils.FLog;
import com.onlinetest.vuquang.filemanager.utils.FileHelper;

import java.text.MessageFormat;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class MoveAction implements ModifyAction {
    private String srcPath;
    private String desPath;

    public MoveAction(String srcPath, String desPath) {
        this.srcPath = srcPath;
        this.desPath = desPath;
    }

    @Override
    public boolean execute() {
        if(FileHelper.moveFile(srcPath, desPath)) {
            logMessage();
            return true;
        }
        return false;
    }

    @Override
    public void logMessage() {
        FLog.show(MessageFormat.format("Move from {0} to {1}", srcPath, desPath));
    }

    @Override
    public boolean undo() {
        return FileHelper.moveFile(desPath,srcPath);
    }
}
