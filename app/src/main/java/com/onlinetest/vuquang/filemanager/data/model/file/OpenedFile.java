package com.onlinetest.vuquang.filemanager.data.model.file;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class OpenedFile {
    private String path;
    private long lastOpenedTime;

    public OpenedFile(String path) {
        this.path = path;
        this.lastOpenedTime = System.currentTimeMillis();
    }

    public String getPath() {
        return path;
    }

    public long getLastOpenedTime() {
        return lastOpenedTime;
    }
}
