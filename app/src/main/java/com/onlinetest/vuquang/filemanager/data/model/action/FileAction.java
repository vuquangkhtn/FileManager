package com.onlinetest.vuquang.filemanager.data.model.action;


/**
 * Created by VuQuang on 6/9/2018.
 */

public interface FileAction {
    boolean undo();
    boolean execute();
    void logMessage();
}