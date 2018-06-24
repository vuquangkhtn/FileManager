package com.onlinetest.vuquang.filemanager.data.model.file;

import android.support.annotation.NonNull;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;

/**
 * Created by VuQuang on 6/23/2018.
 */

public class CustomFolder extends AbstractFile {
    public CustomFolder(@NonNull String pathname) {
        super(pathname);
    }

    @Override
    public long getSize() {
        long totalSize = 0;
        if(this.listFiles().length == 0) {
            return 0;
        }
        for (File file:this.listFiles()) {
            AbstractFile abstractFile = AbstractFile.castType(file.getPath());
            totalSize += abstractFile.getSize();
        }
        return totalSize;
    }

    @Override
    public String getExtension() {
        return "Folder";
    }

    private int countFolderItemsCount() {
        return this.list().length;
    }


    @Override
    public String getDetail() {
        return MessageFormat.format("{0} ({1} items)",getStrLastModified(), countFolderItemsCount());
    }
}
