package com.onlinetest.vuquang.filemanager.main.sort;

import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;
import com.onlinetest.vuquang.filemanager.utils.FLog;

public class FileTypeSort implements AbstractSort {

    @Override
    public int compare(AbstractFile f1, AbstractFile f2) {
        if (f1.isDirectory() && f2.isDirectory())
            return 0;
        else if (f1.isDirectory() && !f2.isDirectory())
            return -1;
        else
            return 1;
    }
    @Override
    public void showLog() {
        FLog.show("sort by file type");
    }
}