package com.onlinetest.vuquang.filemanager.main;

import com.onlinetest.vuquang.filemanager.base.MvpView;
import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;

import java.io.File;
import java.util.List;

/**
 * Created by VuQuang on 6/9/2018.
 */

public interface MainMvpView extends MvpView {
    void updateUI(List<AbstractFile> fileList);

    boolean openFile(AbstractFile file);

    void setEmptyMode(boolean isEnable);

    void notifyDelete(AbstractFile file);
}
