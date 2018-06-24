package com.onlinetest.vuquang.filemanager.data.model.action;

import com.onlinetest.vuquang.filemanager.log.AppLog;
import com.onlinetest.vuquang.filemanager.utils.FileHelper;

import java.io.File;
import java.text.MessageFormat;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class MoveAction implements FileAction {
    private String srcPath;
    private String desDir;

    public MoveAction(String srcPath, String desDir) {
        this.srcPath = srcPath;
        this.desDir = desDir;
    }

    @Override
    public boolean execute() {
        if(FileHelper.moveFile(srcPath, desDir)) {
            logMessage();
            return true;
        }
        return false;
    }

    @Override
    public void logMessage() {
        AppLog.show(MessageFormat.format("Move from {0} to {1}", srcPath, desDir));
    }

    @Override
    public boolean undo() {
        return FileHelper.moveFile(desDir+ File.separator+FileHelper.getFileName(srcPath)
                ,FileHelper.getParentDir(srcPath));
    }
}
