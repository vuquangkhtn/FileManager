package com.onlinetest.vuquang.filemanager.main.sort;

import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;
import com.onlinetest.vuquang.filemanager.log.AppLog;

public class TimeModifiedSort implements AbstractSort {

    @Override
    public int compare(AbstractFile f1, AbstractFile f2) {
        Long time1 = f1.lastModified();
        Long time2 = f2.lastModified();
        return time2.compareTo(time1);
    }
    @Override
    public void showLog() {
        AppLog.show("sort by time modified");
    }
}