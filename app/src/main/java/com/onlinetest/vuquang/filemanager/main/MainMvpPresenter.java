package com.onlinetest.vuquang.filemanager.main;

import com.onlinetest.vuquang.filemanager.base.MvpPresenter;
import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;
import com.onlinetest.vuquang.filemanager.main.sort.AbstractSort;

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

    void openFile(AbstractFile file);

    void deleteFile(AbstractFile file);

    void deleteMultiFiles(List<AbstractFile> files);

    void permanentlyDeleteFile(AbstractFile file);

    void copyFile(String srcFile, String path);

    void moveFile(String srcFile, String path);

    void createFile(String s);

    void createFolder(String s);

    void sort(AbstractSort sort);

    void onBackClicked();

    void copyMultiFiles(List<AbstractFile> selectedList, String path);

    void moveMultiFiles(List<AbstractFile> selectedList, String path);
}
