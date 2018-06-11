package com.onlinetest.vuquang.filemanager.main;

import com.onlinetest.vuquang.filemanager.app.FileManagerApp;
import com.onlinetest.vuquang.filemanager.base.BasePresenter;
import com.onlinetest.vuquang.filemanager.data.manager.AppDataManager;
import com.onlinetest.vuquang.filemanager.data.model.action.CopyAction;
import com.onlinetest.vuquang.filemanager.data.model.action.CreateFileAction;
import com.onlinetest.vuquang.filemanager.data.model.action.CreateFolderAction;
import com.onlinetest.vuquang.filemanager.data.model.action.DeleteAction;
import com.onlinetest.vuquang.filemanager.data.model.action.MoveAction;
import com.onlinetest.vuquang.filemanager.data.model.action.PermanentlyDeleteAction;
import com.onlinetest.vuquang.filemanager.data.model.file.CustomFile;
import com.onlinetest.vuquang.filemanager.utils.FLog;
import com.onlinetest.vuquang.filemanager.utils.FileHelper;
import com.onlinetest.vuquang.filemanager.app.LocalPathUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public void onUndoClicked() {
        if(!getDataManager().getActionManager().undo()) {
            getMvpView().onError("Can't undo");
        } else {
            getMvpView().showMessage("Undo successful");
            //update list at current dir
            openDirectory(new File(FileManagerApp.getApp().getCurPath()));
        }
    }

    @Override
    public void onRedoClicked() {
        if(!getDataManager().getActionManager().redo()) {
            getMvpView().onError("Can't redo");
        }else {
            getMvpView().showMessage("Redo successful");
            //update list at current dir
            openDirectory(new File(FileManagerApp.getApp().getCurPath()));
        }
    }

    @Override
    public void loadExternalStorage() {
        FileManagerApp.getApp().setCurPath(LocalPathUtils.EXTERNAL_STORAGE);
        File file = FileHelper.getFile(LocalPathUtils.EXTERNAL_STORAGE);
        openDirectory(file);
    }

    @Override
    public void loadQuickAccess() {
        fileList = getDataManager().getOpenedFileManager().getRecentFile();
        FileManagerApp.getApp().setCurPath("");
        if(fileList != null && fileList.size() != 0) {
            getMvpView().setEmptyMode(false);
            Collections.sort(fileList, new Comparator<CustomFile>() {
                @Override
                public int compare(CustomFile o1, CustomFile o2) {
                    Long time1 = o1.getLastOpenedTime();
                    Long time2 = o2.getLastOpenedTime();
                    return time2.compareTo(time1);
                }
            });
            getMvpView().updateUI(fileList);
        } else {
            getMvpView().setEmptyMode(true);
        }
    }

    @Override
    public void loadRecycleBin() {
        File file = FileHelper.getFile(LocalPathUtils.RECYCLE_BIN_DIR);
        openDirectory(file);
    }

    @Override
    public void openFile(CustomFile file) {
        if(file.getFile().isDirectory()) {
            openDirectory(file.getFile());
            FLog.show("browse directory "+file.getPath());
        } else {
            if(getMvpView().openFile(file)) {
                getDataManager().getOpenedFileManager().addOpenedFile(file.getPath());
                FLog.show("open file "+file.getPath());
            } else {
                getMvpView().showMessage("This file is not supported");
            }
        }
    }

    @Override
    public void deleteFile(CustomFile file) {
        if(!getDataManager().getActionManager().addAction(new DeleteAction(file.getPath()))) {
            getMvpView().onError("Delete failed");
        } else {
            getMvpView().notifyDelete(file);
            if(!getDataManager().getOpenedFileManager().updateOpenedFile(file.getPath(),
                    LocalPathUtils.RECYCLE_BIN_DIR+File.separator+FileHelper.getFileName(file.getPath()))) {
                getMvpView().showMessage("Can't update quick access");
            }
            getMvpView().showMessage("Delete successful");
        }
    }

    @Override
    public void permanentlyDeleteFile(CustomFile file) {
        if(!getDataManager().getActionManager().addAction(new PermanentlyDeleteAction(file.getPath()))) {
            getMvpView().onError("Permanently Delete failed");
        } else {
            getMvpView().notifyDelete(file);
            getDataManager().getOpenedFileManager().removeIfContain(file.getPath());
            getMvpView().showMessage("Permanently Delete successful");
        }
    }

    @Override
    public void copyFile(String srcFile, String desPath) {
        if(srcFile.equals(desPath)) {
            getMvpView().showMessage("Move failed");
            return;
        }
        if(!getDataManager().getActionManager().addAction(new CopyAction(srcFile, desPath))) {
            getMvpView().onError("Copy failed");
        } else {
            openDirectory(new File(desPath));
            getMvpView().showMessage("Copy successful");
        }
    }

    @Override
    public void moveFile(String srcFile, String desPath) {
        if(srcFile.equals(desPath)) {
            getMvpView().showMessage("Move failed");
            return;
        }

        if(!getDataManager().getActionManager().addAction(new MoveAction(srcFile, desPath))) {
            getMvpView().showMessage("Move failed");
        } else {
            openDirectory(new File(srcFile).getParentFile());
            if(!getDataManager().getOpenedFileManager().updateOpenedFile(srcFile,
                    desPath+File.separator+FileHelper.getFileName(srcFile))) {
                getMvpView().showMessage("Can't update quick access");
            }
            getMvpView().showMessage("Move successful");
        }
    }

    @Override
    public void copyMultiFiles(List<CustomFile> selectedList, String path) {
        for (CustomFile file:selectedList) {
            copyFile(file.getPath(),path);
        }
    }

    @Override
    public void moveMultiFiles(List<CustomFile> selectedList, String path) {
        for (CustomFile file:selectedList) {
            moveFile(file.getPath(),path);
        }
    }

    @Override
    public void deleteMultiFiles(List<CustomFile> selectedList) {
        for (CustomFile file:selectedList) {
            deleteFile(file);
        }
    }

    @Override
    public void createFile(String s) {
        String curPath = FileManagerApp.getApp().getCurPath();
        String filePath = curPath + File.separator + s;
        if(!getDataManager().getActionManager().addAction(new CreateFileAction(filePath))) {
            getMvpView().onError("Create failed");
        } else {
            openDirectory(new File(curPath));
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
            openDirectory(new File(curPath));
            getMvpView().showMessage("Create successful");
        }
    }

    @Override
    public void sortListByName() {
        Collections.sort(fileList, new Comparator<CustomFile>() {
            @Override
            public int compare(CustomFile o1, CustomFile o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        FLog.show("Sort list by Name");
        getMvpView().updateUI(fileList);
    }

    @Override
    public void sortListByCreatedTime() {
        Collections.sort(fileList, new Comparator<CustomFile>() {
            @Override
            public int compare(CustomFile o1, CustomFile o2) {
                Long time1 = o1.getCreatedTime();
                Long time2 = o2.getCreatedTime();
                return time2.compareTo(time1);
            }
        });
        getMvpView().updateUI(fileList);
        FLog.show("Sort list by Created Name");
    }

    @Override
    public void sortListByModifed() {
        Collections.sort(fileList, new Comparator<CustomFile>() {
            @Override
            public int compare(CustomFile o1, CustomFile o2) {
                Long time1 = o1.getFile().lastModified();
                Long time2 = o2.getFile().lastModified();
                return time2.compareTo(time1);
            }
        });
        getMvpView().updateUI(fileList);
        FLog.show("Sort list by Last Modified");
    }

    @Override
    public void sortListByOpenedTime() {
        Collections.sort(fileList, new Comparator<CustomFile>() {
            @Override
            public int compare(CustomFile o1, CustomFile o2) {
                Long time1 = o1.getLastOpenedTime();
                Long time2 = o2.getLastOpenedTime();
                return time2.compareTo(time1);
            }
        });
        getMvpView().updateUI(fileList);
        FLog.show("Sort list by Opened Time");
    }

    @Override
    public void sortListByFileType() {
        defaultSort();
        FLog.show("Sort list by File Type");
    }

    private void defaultSort() {//File Type
        Collections.sort(fileList, new Comparator<CustomFile>() {
            @Override
            public int compare(CustomFile o1, CustomFile o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
        Collections.sort(fileList, new Comparator<CustomFile>() {
            @Override
            public int compare(CustomFile f1, CustomFile f2) {
                if (f1.getFile().isDirectory() == f2.getFile().isDirectory())
                    return 0;
                else if (f1.getFile().isDirectory() && !f2.getFile().isDirectory())
                    return -1;
                else
                    return 1;
            }
        });
        getMvpView().updateUI(fileList);
    }

    @Override
    public void onBackClicked() {
        String curPath = FileManagerApp.getApp().getCurPath();
        if(!curPath.equals(LocalPathUtils.EXTERNAL_STORAGE)) {
            File file = new File(curPath);
            openDirectory(file.getParentFile());
        }
    }

    private void openDirectory(File directory) {
        fileList.clear();
        FileManagerApp.getApp().setCurPath(directory.getPath());
        if(directory.listFiles() != null && directory.listFiles().length != 0) {
            getMvpView().setEmptyMode(false);
            for (File childFile:directory.listFiles()) {
                if(childFile.getName().startsWith(".")) {//hidden file
                    continue;
                }
                CustomFile customFile = new CustomFile(childFile.getPath());
                customFile.setLastOpenedTime(getDataManager().getOpenedFileManager().getOpenTime(childFile.getPath()));
                fileList.add(customFile);
            }
        } else {
            getMvpView().setEmptyMode(true);
        }
        defaultSort();
    }
}
