package com.onlinetest.vuquang.filemanager.data.manager;

import android.content.Context;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class AppDataManager {
    private static AppDataManager instance;
    private ActionManager actionManager;
    private OpenedFileManager openedFileManager;

    public AppDataManager(ActionManager actionManager, OpenedFileManager openedFileManager) {
        this.actionManager = actionManager;
        this.openedFileManager = openedFileManager;
    }

    public static AppDataManager getDataManager(Context context) {
        if(instance == null) {
            instance = new AppDataManager(new ActionManager(), new OpenedFileManager(context));
        }
        return instance;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public OpenedFileManager getOpenedFileManager() {
        return openedFileManager;
    }
}
