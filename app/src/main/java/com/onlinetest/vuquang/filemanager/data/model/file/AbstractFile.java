package com.onlinetest.vuquang.filemanager.data.model.file;

import android.support.annotation.NonNull;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by VuQuang on 6/23/2018.
 */

public abstract class AbstractFile extends File{
    public boolean isSelected = false;
    private long lastOpenedTime;
    public AbstractFile(@NonNull String pathname) {
        super(pathname);
        lastOpenedTime = 0;
    }


    public long getLastOpenedTime() {
        return lastOpenedTime;
    }
    public void setLastOpenedTime(long lastOpenedTime) {
        this.lastOpenedTime = lastOpenedTime;
    }

    public String getMimeType() {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(getPath());
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
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

    public long getCreatedTime() {
        long time = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                BasicFileAttributes attr = Files.readAttributes(this.toPath(), BasicFileAttributes.class);
                time = attr.creationTime().toMillis();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return time;
    }


    public String getStrLastModified() {
        long time = this.lastModified();
        if(time == 0) {
            return "None";
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy,HH:mm");
            return format1.format(cal.getTime());
        }
    }


    public static AbstractFile castType(String path) {
        File file = new File(path);
        if(file.isDirectory()) {
            return new CustomFolder(path);
        } else {
            return new CustomFile(path);
        }
    }



    public String getSizeInfo() {
        String[] measure = {"B", "KB", "MB", "GB"};
        int countMeasure = 0;
        double size = getSize();
        while(size > 1024 && countMeasure < 4) {
            size = size / 1024;
            countMeasure++;
        }
        return String.format("%.2f %s",size, measure[countMeasure]);
    }


    abstract long getSize();
    public abstract String getExtension();
    public abstract String getDetail();
}
