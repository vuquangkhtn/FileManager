package com.onlinetest.vuquang.filemanager.data.model.access;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class OpenAction extends AccessAction{
    private long lastOpenedTime;

    public boolean execute() {
        return false;
    }

    @Override
    public void logMessage() {

    }


    public long getLastOpenedTime() {
        return lastOpenedTime;
    }

    public void setLastOpenedTime(long lastOpenedTime) {
        this.lastOpenedTime = lastOpenedTime;
    }
}
