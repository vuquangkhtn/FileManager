package com.onlinetest.vuquang.filemanager.data.manager;

import android.content.Context;

import com.onlinetest.vuquang.filemanager.data.dao.OpenedFileDAO;
import com.onlinetest.vuquang.filemanager.data.model.file.AbstractFile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class OpenedFileManager {
    private OpenedFileDAO dao;

    OpenedFileManager(Context context) {
        dao = new OpenedFileDAO(context);
    }

    public boolean addOpenedFile(String path) {
        removeIfContain(path);
        AbstractFile file = AbstractFile.castType(path);
        file.setLastOpenedTime(System.currentTimeMillis());
        return dao.insertOpenedFile(file);
    }

    public boolean updateOpenedFile(String oldPath, String curPath) {
        if(contain(oldPath)) {
            long openedTime = dao.getOpenedTime(oldPath);
            removeIfContain(oldPath);
            AbstractFile curFile = AbstractFile.castType(curPath);
            curFile.setLastOpenedTime(openedTime);
            return dao.insertOpenedFile(curFile);
        }
        return true;
    }

    public long getOpenTime(String path) {
        for (AbstractFile file : getAllOpenedFile()) {
            if(file.getPath().equals(path)) {
                return file.getLastOpenedTime();
            }
        }
        return 0;
    }

    public void removeIfContain(String path) {
        if(contain(path)) {
            dao.deleteOpenedFile(path);
        }
    }

    private boolean contain(String path) {
        for (AbstractFile file : getAllOpenedFile()) {
            if(file.getPath().equals(path)) {
                return true;
            }
        }
        return false;
    }

    public List<AbstractFile> getAllOpenedFile() {
        return dao.getAllOpenedFiles();
    }

    public List<AbstractFile> getRecentFile() {
        List<AbstractFile> recentFiles = new ArrayList<>();

        for (AbstractFile file:getAllOpenedFile()) {
            if(System.currentTimeMillis() - file.getLastOpenedTime() <= getMillisOf(2)) {
                recentFiles.add(file);
            }
        }

        return recentFiles;


    }

    public long getMillisOf(int days) {
        return 1000*60*60*24*days;
    }
}
