package com.onlinetest.vuquang.filemanager.main;

import com.onlinetest.vuquang.filemanager.base.MvpView;
import com.onlinetest.vuquang.filemanager.data.model.file.CustomFile;

import java.io.File;
import java.util.List;

/**
 * Created by VuQuang on 6/9/2018.
 */

public interface MainMvpView extends MvpView {
    void updateUI(List<CustomFile> fileList);

    boolean openFile(CustomFile file);

    public void setEmptyMode(boolean isEnable);

    void deleteFile(CustomFile file);
}
