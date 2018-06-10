package com.onlinetest.vuquang.filemanager.data.model.action;

import com.onlinetest.vuquang.filemanager.utils.FLog;
import com.onlinetest.vuquang.filemanager.utils.FileHelper;

import java.io.File;
import java.text.MessageFormat;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class CopyAction implements FileAction {
    private String srcPath;
    private String desDir;

    public CopyAction(String srcPath, String desDir) {
        this.srcPath = srcPath;
        this.desDir = desDir;
    }

    @Override
    public boolean execute() {
        FileHelper.copyFileOrDirectory(srcPath, desDir);
        logMessage();
        return true;
    }

    @Override
    public void logMessage() {
        FLog.show(MessageFormat.format("Copy from {0} to {1}", srcPath, desDir));
    }

    @Override
    public boolean undo() {
        return FileHelper.removeFile(desDir+ File.separator + FileHelper.getFileName(srcPath));
    }
}
