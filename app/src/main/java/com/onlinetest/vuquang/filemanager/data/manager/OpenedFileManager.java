package com.onlinetest.vuquang.filemanager.data.manager;

import com.onlinetest.vuquang.filemanager.data.model.file.OpenedFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class OpenedFileManager {
    private List<OpenedFile> openedFiles = new ArrayList();

    public void addOpenedFile(String path) {
        removeIfContain(path);
        openedFiles.add(new OpenedFile(path));
    }

    private void removeIfContain(String path) {
        for (OpenedFile act: openedFiles) {
            if(act.getPath().equals(path)) {
                openedFiles.remove(act);
            }
        }
    }

    public long getOpenTime(String path) {
        for (OpenedFile file : openedFiles) {
            if(file.getPath().equals(path)) {
                return file.getLastOpenedTime();
            }
        }
        return 0;
    }
}
