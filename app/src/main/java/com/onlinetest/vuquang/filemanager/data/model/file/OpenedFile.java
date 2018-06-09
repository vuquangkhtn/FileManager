package com.onlinetest.vuquang.filemanager.data.model.file;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class OpenedFile {

    private int id;
    private String path;
    private long lastOpenedTime;

    public OpenedFile() {

    }

    public OpenedFile(String path) {
        this.path = path;
        this.lastOpenedTime = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLastOpenedTime() {
        return lastOpenedTime;
    }

    public void setLastOpenedTime(long lastOpenedTime) {
        this.lastOpenedTime = lastOpenedTime;
    }
}
