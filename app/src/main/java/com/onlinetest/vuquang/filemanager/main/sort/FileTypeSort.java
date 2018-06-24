package com.onlinetest.vuquang.filemanager.main.sort;

import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;
import com.onlinetest.vuquang.filemanager.log.AppLog;

public class FileTypeSort implements AbstractSort {

    @Override
    public int compare(AbstractFile f1, AbstractFile f2) {
        if (f1.isDirectory() && f2.isDirectory())
            return (new NameSort()).compare(f1, f1);
        else if (f1.isDirectory())
            return -1;
        else
            return 1;
    }
    @Override
    public void showLog() {
        AppLog.show("sort by file type");
    }
}