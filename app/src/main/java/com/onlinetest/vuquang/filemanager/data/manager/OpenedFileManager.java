package com.onlinetest.vuquang.filemanager.data.manager;

import android.content.Context;

import com.onlinetest.vuquang.filemanager.data.dao.OpenedFileDAO;
import com.onlinetest.vuquang.filemanager.data.model.file.OpenedFile;

import java.util.List;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class OpenedFileManager {
    private OpenedFileDAO dao;

    public OpenedFileManager(Context context) {
        dao = new OpenedFileDAO(context);
    }

    public boolean addOpenedFile(String path) {
        removeIfContain(path);
        return dao.insertOpenedFile(new OpenedFile(path));
    }

    public long getOpenTime(String path) {
        for (OpenedFile file : getAllOpenedFile()) {
            if(file.getPath().equals(path)) {
                return file.getLastOpenedTime();
            }
        }
        return 0;
    }

    private void removeIfContain(String path) {
        for (OpenedFile file : getAllOpenedFile()) {
            if(file.getPath().equals(path)) {
                dao.deleteOpenedFile(path);
                return;
            }
        }
    }

    public List<OpenedFile> getAllOpenedFile() {
        return dao.getAllOpenedFiles();
    }

}
