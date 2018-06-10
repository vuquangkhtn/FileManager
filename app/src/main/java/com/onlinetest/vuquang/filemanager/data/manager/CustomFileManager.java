package com.onlinetest.vuquang.filemanager.data.manager;

import android.content.Context;

import com.onlinetest.vuquang.filemanager.data.dao.OpenedFileDAO;
import com.onlinetest.vuquang.filemanager.data.model.file.CustomFile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class CustomFileManager {
    private OpenedFileDAO dao;

    public CustomFileManager(Context context) {
        dao = new OpenedFileDAO(context);
    }

    public boolean addOpenedFile(String path) {
        removeIfContain(path);
        CustomFile file = new CustomFile(path);
        file.setLastOpenedTime(System.currentTimeMillis());
        return dao.insertOpenedFile(file);
    }

    public long getOpenTime(String path) {
        for (CustomFile file : getAllOpenedFile()) {
            if(file.getPath().equals(path)) {
                return file.getLastOpenedTime();
            }
        }
        return 0;
    }

    private void removeIfContain(String path) {
        for (CustomFile file : getAllOpenedFile()) {
            if(file.getPath().equals(path)) {
                dao.deleteOpenedFile(path);
                return;
            }
        }
    }

    public List<CustomFile> getAllOpenedFile() {
        return dao.getAllOpenedFiles();
    }

    public List<CustomFile> getRecentFile() {
        List<CustomFile> recentFiles = new ArrayList<>();

        for (CustomFile file:getAllOpenedFile()) {
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
