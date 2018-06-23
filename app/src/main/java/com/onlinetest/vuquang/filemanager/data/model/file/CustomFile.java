package com.onlinetest.vuquang.filemanager.data.model.file;

import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import com.onlinetest.vuquang.filemanager.utils.FileHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class CustomFile extends AbstractFile {

    public CustomFile(@NonNull String pathname) {
        super(pathname);
    }

    public String getExtension() {
        return MimeTypeMap.getFileExtensionFromUrl(getPath());
    }

    @Override
    public String getStrSize() {
        String[] measure = {"B", "KB", "MB", "GB"};
        int countMeasure = 0;
        double size = getSize();
        while(size > 1024 && countMeasure < 4) {
            size = size / 1024;
            countMeasure++;
        }
        return String.format("%.2f %s",size, measure[countMeasure]);
    }

    @Override
    public long getSize() {
        return this.length();
    }
}
