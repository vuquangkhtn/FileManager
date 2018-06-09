package com.onlinetest.vuquang.filemanager.data.model.access;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class OpenAction {
    private String path;
    private long lastOpenedTime;

    public boolean execute() {
        return false;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
