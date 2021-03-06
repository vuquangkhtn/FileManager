package com.onlinetest.vuquang.filemanager.data.model.action;

import com.onlinetest.vuquang.filemanager.log.AppLog;
import com.onlinetest.vuquang.filemanager.utils.FileHelper;
import com.onlinetest.vuquang.filemanager.app.LocalPathUtils;

import java.io.File;
import java.text.MessageFormat;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class DeleteAction implements FileAction {
    private String path;

    public DeleteAction(String path) {
        this.path = path;
    }

    @Override
    public boolean execute() {
        if(FileHelper.moveFile(path, LocalPathUtils.RECYCLE_BIN_DIR)) {
            logMessage();
            return true;
        }
        return false;
    }

    @Override
    public void logMessage() {
        AppLog.show(MessageFormat.format("Delete {0}", path));

    }

    @Override
    public boolean undo() {
        return FileHelper.moveFile(LocalPathUtils.RECYCLE_BIN_DIR+ File.separator+FileHelper.getFileName(path)
                ,FileHelper.getParentDir(path));
    }
}
