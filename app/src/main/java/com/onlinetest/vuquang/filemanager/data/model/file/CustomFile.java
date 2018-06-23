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

    @Override
    public String getDetail() {
        return MessageFormat.format("{0} ({1})",getStrLastModified(), getSizeInfo());
    }

    public String getExtension() {
        return MimeTypeMap.getFileExtensionFromUrl(getPath());
    }

    @Override
    public long getSize() {
        return this.length();
    }
}
