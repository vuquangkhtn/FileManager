package com.onlinetest.vuquang.filemanager.data.model.file;

import com.onlinetest.vuquang.filemanager.utils.FileHelper;

import java.io.File;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class CustomFile {

    private int id;
    private File file;
    private long lastOpenedTime;

    public CustomFile() {
        id = -1;
        file = null;
        lastOpenedTime = 0;
    }

    public CustomFile(String path) {
        this.file = FileHelper.getFile(path);
        lastOpenedTime = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return file.getPath();
    }

    public void setPath(String path) {
        this.file = FileHelper.getFile(path);
    }

    public long getLastOpenedTime() {
        return lastOpenedTime;
    }

    public void setLastOpenedTime(long lastOpenedTime) {
        this.lastOpenedTime = lastOpenedTime;
    }
}
