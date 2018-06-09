package com.onlinetest.vuquang.filemanager.data.manager;

import android.content.Context;

import com.onlinetest.vuquang.filemanager.data.dao.OpenedFileDAO;
import com.onlinetest.vuquang.filemanager.data.model.file.CustomFile;

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

}
