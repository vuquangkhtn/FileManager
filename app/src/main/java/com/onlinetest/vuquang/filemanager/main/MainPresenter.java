package com.onlinetest.vuquang.filemanager.main;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.onlinetest.vuquang.filemanager.app.FileManagerApp;
import com.onlinetest.vuquang.filemanager.base.BasePresenter;
import com.onlinetest.vuquang.filemanager.data.manager.AppDataManager;
import com.onlinetest.vuquang.filemanager.data.model.action.CopyAction;
import com.onlinetest.vuquang.filemanager.data.model.action.CreateFileAction;
import com.onlinetest.vuquang.filemanager.data.model.action.CreateFolderAction;
import com.onlinetest.vuquang.filemanager.data.model.action.DeleteAction;
import com.onlinetest.vuquang.filemanager.data.model.action.MoveAction;
import com.onlinetest.vuquang.filemanager.data.model.action.PermanentlyDeleteAction;
import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;
import com.onlinetest.vuquang.filemanager.log.AppLog;
import com.onlinetest.vuquang.filemanager.main.sort.AbstractSort;
import com.onlinetest.vuquang.filemanager.main.sort.FileTypeSort;
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
    private static final int ERROR_UNDO_ACTION = 10;
    private static final int ERROR_REDO_ACTION = 11;
    private static final int ERROR_COPY_ACTION = 12;
    private static final int ERROR_MOVE_ACTION = 13;
    private static final int ERROR_DELETE_ACTION = 14;
    private static final int ERROR_PERMANENTLY_DELETE_ACTION = 16;
    private static final int SUCCESS_UNDO_ACTION = 20;
    private static final int SUCCESS_REDO_ACTION = 21;
    private static final int SUCCESS_COPY_ACTION = 22;
    private static final int SUCCESS_MOVE_ACTION = 23;
    private static final int SUCCESS_DELETE_ACTION = 24;
    private static final int SUCCESS_PERMANENTLY_DELETE_ACTION = 26;
    private static final int MULTI_DELETE_ACTION = 30;
    private static final int MULTI_COPY_ACTION = 31;
    private static final int MULTI_MOVE_ACTION = 32;

    private AbstractSort sort;
    private List<AbstractFile> fileList;
    private Handler mainHandler;

    public MainPresenter(AppDataManager dataManager) {
        super(dataManager);
        fileList = new ArrayList<>();
        sort = new FileTypeSort();
        mainHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                getMvpView().hideLoading();
                switch (msg.what) {
                    case ERROR_UNDO_ACTION: {
                        getMvpView().onError("Can't undo");
                        return true;
                    }
                    case ERROR_REDO_ACTION: {
                        getMvpView().onError("Can't redo");
                        break;
                    }
                    case ERROR_COPY_ACTION: {
                        getMvpView().showMessage("Copy failed");
                        break;
                    }
                    case ERROR_MOVE_ACTION: {
                        getMvpView().showMessage("Move failed");
                        break;
                    }
                    case ERROR_DELETE_ACTION: {
                        getMvpView().onError("Delete failed");
                        break;
                    }
                    case ERROR_PERMANENTLY_DELETE_ACTION: {
                        break;
                    }
                    case SUCCESS_UNDO_ACTION: {
                        openDirectory(new File(FileManagerApp.getApp().getCurPath()));
                        getMvpView().showMessage("Undo successful");
                        return true;
                    }
                    case SUCCESS_REDO_ACTION: {
                        openDirectory(new File(FileManagerApp.getApp().getCurPath()));
                        getMvpView().showMessage("Redo successful");
                        break;
                    }
                    case SUCCESS_COPY_ACTION: {
                        String desPath;
                        if(msg.obj != null) {
                            desPath = (String) msg.obj;
                        } else {
                            return false;
                        }
                        openDirectory(new File(desPath));
                        getMvpView().showMessage("Copy successful");
                        break;
                    }
                    case SUCCESS_MOVE_ACTION: {
                        String srcFile;
                        if(msg.obj != null) {
                            srcFile = (String) msg.obj;
                        } else {
                            return false;
                        }
                        openDirectory(new File(srcFile).getParentFile());
                        getMvpView().showMessage("Move successful");
                        break;
                    }
                    case SUCCESS_DELETE_ACTION: {
                        AbstractFile file;
                        if(msg.obj != null) {
                            file = (AbstractFile) msg.obj;
                        } else {
                            Log.d("ERROR","not input obj file for delete action");
                            return false;
                        }
                        getMvpView().notifyDelete(file);

                        getMvpView().showMessage("Delete successful");
                        break;
                    }
                    case SUCCESS_PERMANENTLY_DELETE_ACTION: {
                        AbstractFile file;
                        if(msg.obj != null) {
                            file = (AbstractFile) msg.obj;
                        } else {
                            Log.d("ERROR","not input obj file for permanently delete action");
                            return false;
                        }
                        getMvpView().notifyDelete(file);
                        getDataManager().getOpenedFileManager().removeIfContain(file.getPath());
                        getMvpView().showMessage("Permanently Delete successful");
                        break;
                    }
                    case MULTI_COPY_ACTION: {
                        getMvpView().showMessage("Multi Copy completed");
                        break;
                    }
                    case MULTI_DELETE_ACTION: {
                        String srcFile;
                        if(msg.obj != null) {
                            srcFile = (String) msg.obj;
                        } else {
                            return false;
                        }
                        openDirectory(new File(srcFile).getParentFile());
                        getMvpView().showMessage("Multi Delete completed");
                        break;
                    }
                    case MULTI_MOVE_ACTION: {
                        String srcFile;
                        if(msg.obj != null) {
                            srcFile = (String) msg.obj;
                        } else {
                            return false;
                        }
                        openDirectory(new File(srcFile).getParentFile());
                        getMvpView().showMessage("Multi Move completed");
                        break;
                    }
                }

                return false;
            }

        });
    }

    @Override
    public void onUndoClicked() {
        getMvpView().showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!getDataManager().getActionManager().undo()) {
                    mainHandler.obtainMessage(ERROR_UNDO_ACTION, null).sendToTarget();
                } else {
                    mainHandler.obtainMessage(SUCCESS_UNDO_ACTION, null).sendToTarget();
                }
            }
        }).start();
    }

    @Override
    public void onRedoClicked() {
        getMvpView().showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!getDataManager().getActionManager().redo()) {
                    mainHandler.obtainMessage(ERROR_REDO_ACTION, null).sendToTarget();
                }else {
                    mainHandler.obtainMessage(SUCCESS_REDO_ACTION, null).sendToTarget();
                }
            }
        }).start();
    }


    @Override
    public void deleteFile(final AbstractFile file) {
        getMvpView().showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!getDataManager().getActionManager().addAction(new DeleteAction(file.getPath()))) {
                    mainHandler.obtainMessage(ERROR_DELETE_ACTION, file).sendToTarget();
                } else {
                    mainHandler.obtainMessage(SUCCESS_DELETE_ACTION, file).sendToTarget();
                }
            }
        }).start();
    }

    @Override
    public void permanentlyDeleteFile(final AbstractFile file) {
        getMvpView().showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(!getDataManager().getActionManager().addAction(new PermanentlyDeleteAction(file.getPath()))) {
                    mainHandler.obtainMessage(SUCCESS_PERMANENTLY_DELETE_ACTION, file).sendToTarget();
                } else {
                    mainHandler.obtainMessage(SUCCESS_PERMANENTLY_DELETE_ACTION, file).sendToTarget();
                }
            }
        }).start();
    }

    @Override
    public void copyFile(final String srcFile, final String desPath) {
        getMvpView().showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(srcFile.equals(desPath)) {
                    mainHandler.obtainMessage(ERROR_COPY_ACTION, null).sendToTarget();
                    return;
                }
                if(!getDataManager().getActionManager().addAction(new CopyAction(srcFile, desPath))) {
                    mainHandler.obtainMessage(ERROR_COPY_ACTION, null).sendToTarget();
                } else {
                    mainHandler.obtainMessage(SUCCESS_COPY_ACTION, desPath).sendToTarget();
                }
            }
        }).start();
    }

    @Override
    public void moveFile(final String srcFile, final String desPath) {
        getMvpView().showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(srcFile.equals(desPath)) {
                    mainHandler.obtainMessage(ERROR_MOVE_ACTION, null).sendToTarget();
                    return;
                }

                if(!getDataManager().getActionManager().addAction(new MoveAction(srcFile, desPath))) {
                    mainHandler.obtainMessage(ERROR_MOVE_ACTION, null).sendToTarget();
                } else {
            getDataManager().getOpenedFileManager().updateOpenedFile(srcFile,
                    desPath+File.separator+FileHelper.getFileName(srcFile));
                    mainHandler.obtainMessage(SUCCESS_MOVE_ACTION, srcFile).sendToTarget();
                }
            }
        }).start();
    }

    @Override
    public void copyMultiFiles(final List<AbstractFile> selectedList, final String path) {
        getMvpView().showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (AbstractFile file:selectedList) {
                    if(file.getPath().equals(path)) {
                        continue;
                    }
                    getDataManager().getActionManager().addAction(new CopyAction(file.getPath(),path));
                }
                mainHandler.obtainMessage(MULTI_COPY_ACTION).sendToTarget();
            }
        }).start();

    }

    @Override
    public void moveMultiFiles(final List<AbstractFile> selectedList, final String path) {
        getMvpView().showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (AbstractFile file:selectedList) {
                    if(file.getPath().equals(path)) {
                        continue;
                    }
                    getDataManager().getActionManager().addAction(new MoveAction(file.getPath(), path));
                }
                mainHandler.obtainMessage(MULTI_MOVE_ACTION,selectedList.get(0).getPath()).sendToTarget();
            }
        }).start();

    }

    @Override
    public void deleteMultiFiles(final List<AbstractFile> selectedList) {
        getMvpView().showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (AbstractFile file:selectedList) {
                    getDataManager().getActionManager().addAction(new DeleteAction(file.getPath()));
                }
                mainHandler.obtainMessage(MULTI_DELETE_ACTION,selectedList.get(0).getPath()).sendToTarget();
            }
        }).start();

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
    public void sort(AbstractSort abstractSort) {
        if(abstractSort != null) {
            this.sort = abstractSort;
        }
        Collections.sort(fileList, new Comparator<AbstractFile>() {
            @Override
            public int compare(AbstractFile o1, AbstractFile o2) {
                return sort.compare(o1,o2);
            }
        });
        getMvpView().updateUI(fileList);
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
            Collections.sort(fileList, new Comparator<AbstractFile>() {
                @Override
                public int compare(AbstractFile o1, AbstractFile o2) {
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
    public void openFile(AbstractFile file) {
        if(file.isDirectory()) {
            getDataManager().getOpenedFileManager().addOpenedFile(file.getPath());
            openDirectory(file);
            AppLog.show("browse directory "+file.getPath());
        } else {
            if(getMvpView().openFile(file)) {
                getDataManager().getOpenedFileManager().addOpenedFile(file.getPath());
                AppLog.show("open file "+file.getPath());
            } else {
                getMvpView().showMessage("This file is not supported");
            }
        }
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
                AbstractFile abstractFile = AbstractFile.castType(childFile.getPath());
                abstractFile.setLastOpenedTime(getDataManager().getOpenedFileManager().getOpenTime(abstractFile.getPath()));
                fileList.add(abstractFile);
            }
        } else {
            getMvpView().setEmptyMode(true);
        }

        sort(sort);
    }
}
