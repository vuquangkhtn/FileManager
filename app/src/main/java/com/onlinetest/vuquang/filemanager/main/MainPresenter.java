package com.onlinetest.vuquang.filemanager.main;

import com.onlinetest.vuquang.filemanager.base.BasePresenter;
import com.onlinetest.vuquang.filemanager.data.manager.AppDataManager;
import com.onlinetest.vuquang.filemanager.data.model.file.CustomFile;
import com.onlinetest.vuquang.filemanager.utils.FileHelper;
import com.onlinetest.vuquang.filemanager.utils.LocalPathUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V> {

    private List<CustomFile> fileList;

    public MainPresenter(AppDataManager dataManager) {
        super(dataManager);
        fileList = new ArrayList<>();
    }

    @Override
    public void loadExternalStorage() {
        File file = FileHelper.getFile(LocalPathUtils.EXTERNAL_STORAGE);
        for (File childFile:file.listFiles()) {
            CustomFile customFile = new CustomFile(childFile.getPath());
            customFile.setLastOpenedTime(getDataManager().getCustomFileManager().getOpenTime(childFile.getPath()));
            fileList.add(customFile);
        }
        getMvpView().updateUI(fileList);
    }

    @Override
    public void onUndoClicked() {
        if(!getDataManager().getActionManager().undo()) {
            getMvpView().onError("Can't undo");
        }
    }

    @Override
    public void onRedoClicked() {
        if(!getDataManager().getActionManager().redo()) {
            getMvpView().onError("Can't redo");
        }
    }

    @Override
    public void loadQuickAccess() {

    }

    @Override
    public void recycleBin() {

    }
}
