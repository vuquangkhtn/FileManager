package com.onlinetest.vuquang.filemanager.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class FileHelper {
    private static final String TAG = "FileHelper";

    public static boolean isExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static boolean createFileOrDirectory(String path) {
        boolean createFile = false;
        File file = new File(path);
        if (file.isDirectory()) {
            createFile = file.mkdirs();
        } else {
            try {
                createFile = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return createFile;
    }

    public static boolean removeFile(String path) {
        File file = new File(path);
        boolean deleteFile = file.delete();
        if(!deleteFile) {
            Log.d(TAG,"Can't delete file");
            return false;
        } else {
            return true;
        }
    }

    public static void copyFileOrDirectory(String srcFile, String dstDir) {
        File desFolder = new File(dstDir);
        if(!desFolder.isDirectory()) {
            Log.d(TAG,"desDir is not a directory");
            return;
        }

        File src = new File(srcFile);
        File dst = new File(dstDir, src.getName());

        try {
            if (src.isDirectory()) {
                for (String filePath : src.list()) {
                    String src1 = (new File(src, filePath).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);
                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists()) {
            if(destFile.getParentFile().mkdirs()) {
                Log.d(TAG,"create parent folder failed");
                return;
            }
        }

        if (!destFile.exists()) {
            if(destFile.createNewFile()) {
                Log.d(TAG,"create new file failed");
                return;
            }
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    static boolean appendStrToFile(String path, String message) {
        if(!isExist(path))
        {
            if(!createFileOrDirectory(path)) {
                return false;
            }
        }

        boolean isSuccess;
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(path, true);
            bw = new BufferedWriter(fw);

            bw.write(message+"\n");
            isSuccess = true;
        }
        catch(Exception e) {
            isSuccess = false;
            e.printStackTrace();
        }
        finally {
            try {

                if (bw != null) {
                    bw.close();
                }

                if (fw != null) {
                    fw.close();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return isSuccess;
    }

    public static boolean moveFile(String srcPath, String desDir) {
        copyFileOrDirectory(srcPath, desDir);
        return removeFile(srcPath);
    }

    public static String getParentDir(String filePath) {
        File file = new File(filePath);
        return file.getParent();
    }

    public static String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

    public static File getFile(String path) {
        File file = new File(path);
        return file;
    }

}
