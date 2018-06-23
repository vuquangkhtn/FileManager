package com.onlinetest.vuquang.filemanager.data.model.file;

import android.support.annotation.NonNull;

import java.io.File;
import java.text.MessageFormat;

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
        for (String path:
             this.list()) {
            File childFile = new File(path);
            AbstractFile abstractFile = (AbstractFile) childFile;
            totalSize += abstractFile.getSize();
        }
        return totalSize;
    }

    @Override
    public String getSizeInfo() {
        return String.valueOf(countFolderItemsCount());
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
        return MessageFormat.format("{0} ({1})",getStrLastModified(), countFolderItemsCount());
    }
}
