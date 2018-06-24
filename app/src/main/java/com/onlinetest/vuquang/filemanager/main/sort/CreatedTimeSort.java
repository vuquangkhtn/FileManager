package com.onlinetest.vuquang.filemanager.main.sort;

import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;
import com.onlinetest.vuquang.filemanager.utils.FLog;

public class CreatedTimeSort implements AbstractSort {

    @Override
    public int compare(AbstractFile f1, AbstractFile f2) {
        Long time1 = f1.getCreatedTime();
        Long time2 = f2.getCreatedTime();
        return time2.compareTo(time1);
    }

    @Override
    public void showLog() {
        FLog.show("sort by created time");
    }
}