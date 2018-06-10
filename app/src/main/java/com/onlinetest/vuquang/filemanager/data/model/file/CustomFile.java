package com.onlinetest.vuquang.filemanager.data.model.file;

import android.webkit.MimeTypeMap;

import com.onlinetest.vuquang.filemanager.utils.FileHelper;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class CustomFile {

    private int id;
    private File file;
    private long lastOpenedTime;

    public CustomFile() {
        id = -1;
        file = null;
        lastOpenedTime = 0;
    }

    public CustomFile(String path) {
        this.file = FileHelper.getFile(path);
        lastOpenedTime = 0;
    }

    public CustomFile(File file) {
        this.file = file;
        lastOpenedTime = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return file.getPath();
    }

    public File getFile() {
        return file;
    }

    public void setPath(String path) {
        this.file = FileHelper.getFile(path);
    }

    public long getLastOpenedTime() {
        return lastOpenedTime;
    }

    public void setLastOpenedTime(long lastOpenedTime) {
        this.lastOpenedTime = lastOpenedTime;
    }

    public String getName() {
        return file.getName();
    }

    public String getInfo() {
        return MessageFormat.format("{0} ({1} items)",getLastModified(), getFileSize());
    }

    public String getLastModified() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(file.lastModified());
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy,HH:mm");
        return format1.format(cal.getTime());
    }

    public String getFileSize() {
        if(file.isDirectory()) {
            int size = 0;
            if(file.list() != null) {
                size = file.list().length;
            }
            return String.valueOf(size);
        } else {
            return String.valueOf(file.getUsableSpace());
        }
    }

    public String getExtension() {
        return MimeTypeMap.getFileExtensionFromUrl(getPath());
    }

    public String getMimeType() {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(getPath());
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
