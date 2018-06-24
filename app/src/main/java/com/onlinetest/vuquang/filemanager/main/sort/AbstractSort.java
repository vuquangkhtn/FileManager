package com.onlinetest.vuquang.filemanager.main.sort;

import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;

/**
 * Created by VuQuang on 6/24/2018.
 */

public interface AbstractSort {
    int compare(AbstractFile f1, AbstractFile f2);
    void showLog();
}
