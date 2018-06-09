package com.onlinetest.vuquang.filemanager.data.manager;

import android.content.Context;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class AppDataManager {
    private static AppDataManager instance;
    private ActionManager actionManager;
    private CustomFileManager customFileManager;

    public AppDataManager(ActionManager actionManager, CustomFileManager customFileManager) {
        this.actionManager = actionManager;
        this.customFileManager = customFileManager;
    }

    public static AppDataManager getDataManager(Context context) {
        if(instance == null) {
            instance = new AppDataManager(new ActionManager(), new CustomFileManager(context));
        }
        return instance;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public CustomFileManager getCustomFileManager() {
        return customFileManager;
    }
}
