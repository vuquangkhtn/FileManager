package com.onlinetest.vuquang.filemanager.main;

import com.onlinetest.vuquang.filemanager.app.FileManagerApp;
import com.onlinetest.vuquang.filemanager.base.BasePresenter;
import com.onlinetest.vuquang.filemanager.data.manager.AppDataManager;
import com.onlinetest.vuquang.filemanager.data.model.action.CopyAction;
import com.onlinetest.vuquang.filemanager.data.model.action.CreateFileAction;
import com.onlinetest.vuquang.filemanager.data.model.action.CreateFolderAction;
import com.onlinetest.vuquang.filemanager.data.model.action.DeleteAction;
import com.onlinetest.vuquang.filemanager.data.model.action.PermanentlyDeleteAction;
import com.onlinetest.vuquang.filemanager.data.model.file.CustomFile;
import com.onlinetest.vuquang.filemanager.utils.FLog;
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
        updateList(file);
    }

    @Override
    public void onUndoClicked() {
        if(!getDataManager().getActionManager().undo()) {
            getMvpView().onError("Can't undo");
        } else {
            getMvpView().showMessage("Undo successful");
            //update list at current dir
            updateList(new File(FileManagerApp.getApp().getCurPath()));
        }
    }

    @Override
    public void onRedoClicked() {
        if(!getDataManager().getActionManager().redo()) {
            getMvpView().onError("Can't redo");
        }else {
            getMvpView().showMessage("Redo successful");
            //update list at current dir
            updateList(new File(FileManagerApp.getApp().getCurPath()));
        }
    }

    @Override
    public void loadQuickAccess() {
        fileList = getDataManager().getCustomFileManager().getRecentFile();
        getMvpView().updateUI(fileList);
    }

    @Override
    public void loadRecycleBin() {
        File file = FileHelper.getRecycleBin();
        updateList(file);
    }

    @Override
    public void openFile(CustomFile file) {
        if(file.getFile().isDirectory()) {
            updateList(file.getFile());
            FLog.show("browse directory "+file.getPath());
        } else {
            getMvpView().openFile(file);
            FLog.show("open file "+file.getPath());
        }
    }

    @Override
    public void deleteFile(CustomFile file) {
        if(!getDataManager().getActionManager().addAction(new DeleteAction(file.getPath()))) {
            getMvpView().onError("Delete failed");
        } else {
            File parent = file.getFile().getParentFile();
            updateList(parent);
            getMvpView().showMessage("Delete successful");
        }
    }

    @Override
    public void permanentlyDeleteFile(CustomFile file) {
        if(!getDataManager().getActionManager().addAction(new PermanentlyDeleteAction(file.getPath()))) {
            getMvpView().onError("Permanently Delete failed");
        } else {
            File parent = file.getFile().getParentFile();
            updateList(parent);
            getMvpView().showMessage("Permanently Delete successful");
        }
    }

    @Override
    public void showProperties(CustomFile file) {

    }

    @Override
    public void doCopy(String srcFile, String desPath) {
        if(!getDataManager().getActionManager().addAction(new CopyAction(srcFile, desPath))) {
            getMvpView().onError("Copy failed");
        } else {
            updateList(new File(desPath));
            getMvpView().showMessage("Copy successful");
        }
    }

    @Override
    public void createFile(String s) {
        String curPath = FileManagerApp.getApp().getCurPath();
        String filePath = curPath + File.separator + s;
        if(!getDataManager().getActionManager().addAction(new CreateFileAction(filePath))) {
            getMvpView().onError("Create failed");
        } else {
            updateList(new File(curPath));
            getMvpView().showMessage("Create successful");
        }
    }

    @Override
    public void createFolder(String s) {
        String curPath = FileManagerApp.getApp().getCurPath();
        String filePath = curPath + File.separator + s;
        if(!getDataManager().getActionManager().addAction(new CreateFolderAction(filePath))) {
            getMvpView().onError("Create failed");
        } else {
            updateList(new File(curPath));
            getMvpView().showMessage("Create successful");
        }
    }

    @Override
    public void sortListByName() {

    }

    @Override
    public void sortListByCreatedTime() {

    }

    @Override
    public void sortListByModifed() {

    }

    @Override
    public void sortListByOpenedTime() {

    }

    @Override
    public void sortListByFileType() {

    }

    private void updateList(File directory) {
        fileList.clear();
        FileManagerApp.getApp().setCurPath(directory.getPath());
        if(directory.listFiles() != null && directory.listFiles().length != 0) {
            for (File childFile:directory.listFiles()) {
                CustomFile customFile = new CustomFile(childFile.getPath());
                customFile.setLastOpenedTime(getDataManager().getCustomFileManager().getOpenTime(childFile.getPath()));
                fileList.add(customFile);
            }
        } else {
            getMvpView().updateEmptyListUI();
        }
        getMvpView().updateUI(fileList);
    }
}
