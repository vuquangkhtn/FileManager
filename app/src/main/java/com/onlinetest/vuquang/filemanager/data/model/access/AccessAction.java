package com.onlinetest.vuquang.filemanager.data.model.access;

import com.onlinetest.vuquang.filemanager.data.model.FileAction;

/**
 * Created by VuQuang on 6/9/2018.
 */

public abstract class AccessAction implements FileAction{
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
