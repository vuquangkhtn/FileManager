package com.onlinetest.vuquang.filemanager.main.sort;

import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;
import com.onlinetest.vuquang.filemanager.utils.FLog;

/**
 * Created by VuQuang on 6/24/2018.
 */

public class NameSort implements AbstractSort {
    @Override
    public int compare(AbstractFile a, AbstractFile b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
    @Override
    public void showLog() {
        FLog.show("sort by name");
    }
}