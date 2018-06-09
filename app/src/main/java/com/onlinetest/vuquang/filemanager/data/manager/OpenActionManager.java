package com.onlinetest.vuquang.filemanager.data.manager;

import com.onlinetest.vuquang.filemanager.data.model.access.OpenAction;
import com.onlinetest.vuquang.filemanager.data.model.modify.ModifyAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class OpenActionManager {
    private List<OpenAction> actionList = new ArrayList();

    public boolean addAction(OpenAction action) {
        removeIfContain(action);

        if(action.execute()) {
            actionList.add(action);
            return true;
        }
        return false;
    }

    private void removeIfContain(OpenAction action) {
        for (OpenAction act: actionList) {
            if(act.getPath().equals(action.getPath())) {
                actionList.remove(act);
            }
        }
    }

    public long getOpenTime(String path) {
        for (OpenAction act: actionList) {
            if(act.getPath().equals(path)) {
                return act.getLastOpenedTime();
            }
        }
        return 0;
    }
}
