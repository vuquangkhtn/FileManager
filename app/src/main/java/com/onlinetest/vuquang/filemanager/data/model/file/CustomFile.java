package com.onlinetest.vuquang.filemanager.data.model.file;

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

public class CustomFile {

    private int id;
    private File file;
    private long lastOpenedTime;
    public boolean isSelected = false;

    public CustomFile() {
        id = -1;
        file = null;
        lastOpenedTime = 0;
    }

    public CustomFile(String path) {
        this.file = FileHelper.getFile(path);
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

    public long getCreatedTime() {
        long time = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                time = attr.creationTime().toMillis();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return time;
    }

    public String getStrCreatedTime() {
        long time = getCreatedTime();
        if(time == 0) {
            return "None";
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy,HH:mm");
            return format1.format(cal.getTime());
        }
    }

    public String getStrLastOpenedTime() {
        long time = getLastOpenedTime();
        if(time == 0) {
            return "None";
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy,HH:mm");
            return format1.format(cal.getTime());
        }

    }

    public String getLastModified() {
        long time = file.lastModified();
        if(time == 0) {
            return "None";
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy,HH:mm");
            return format1.format(cal.getTime());
        }
    }

    public String getName() {
        return file.getName();
    }

    public String getInfo() {
        return MessageFormat.format("{0} ({1})",getLastModified(), getFileSize());
    }

    public String getFileSize() {
        if(file.isDirectory()) {
            int size = 0;
            if(file.list() != null) {
                size = file.list().length;
            }
            return String.valueOf(size + " items");
        } else {
            return String.valueOf(getDataSize());
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

    private String getDataSize() {
        String[] measure = {"B", "KB", "MB", "GB"};
        int countMeasure = 0;
        double size = file.length();
        while(size > 1024 && countMeasure < 4) {
            size = size / 1024;
            countMeasure++;
        }
        return String.format("%.2f %s",size, measure[countMeasure]);
    }
}
