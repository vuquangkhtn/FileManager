package com.onlinetest.vuquang.filemanager.main;

import com.onlinetest.vuquang.filemanager.base.MvpPresenter;
import com.onlinetest.vuquang.filemanager.data.model.file.CustomFile;

/**
 * Created by VuQuang on 6/9/2018.
 */

public interface MainMvpPresenter<V extends MainMvpView> extends MvpPresenter<V> {
    void loadExternalStorage();
    void onUndoClicked();
    void onRedoClicked();

    void loadQuickAccess();

    void loadRecycleBin();

    void openFile(CustomFile file);

    void deleteFile(CustomFile file);

    void permanentlyDeleteFile(CustomFile file);

    void showProperties(CustomFile file);
}
