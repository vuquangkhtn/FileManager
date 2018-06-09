package com.onlinetest.vuquang.filemanager.data.model.modify;


import com.onlinetest.vuquang.filemanager.data.model.FileAction;

/**
 * Created by VuQuang on 6/9/2018.
 */

public interface ModifyAction extends FileAction {
    public abstract boolean undo();
}