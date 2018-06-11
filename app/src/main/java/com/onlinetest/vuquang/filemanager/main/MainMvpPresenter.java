package com.onlinetest.vuquang.filemanager.main;

import com.onlinetest.vuquang.filemanager.base.MvpPresenter;
import com.onlinetest.vuquang.filemanager.data.model.file.CustomFile;

import java.util.List;

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

    void deleteMultiFiles(List<CustomFile> files);

    void permanentlyDeleteFile(CustomFile file);

    void copyFile(String srcFile, String path);

    void moveFile(String srcFile, String path);

    void createFile(String s);

    void createFolder(String s);

    void sortListByName();

    void sortListByCreatedTime();

    void sortListByModifed();

    void sortListByOpenedTime();

    void sortListByFileType();

    void onBackClicked();

    void copyMultiFiles(List<CustomFile> selectedList, String path);

    void moveMultiFiles(List<CustomFile> selectedList, String path);
}
