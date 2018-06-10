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
        boolean createFile;
        File file = new File(path);
        createFile = file.mkdirs();
        if(!getExtension(getFileName(path)).isEmpty()) {
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
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(null!=files){
                for(File childFile: files) {
                    removeFile(childFile.getPath());
                }
            }
        }
        return(file.delete());
    }

    public static void copyFileOrDirectory(String srcFile, String dstDir) {
        File desFolder = new File(dstDir);
        if(!desFolder.isDirectory()) {
            Log.d(TAG,"desDir is not a directory");
            return;
        }

        File src = new File(srcFile);
        File dst = new File(dstDir, src.getName());

        if (src.isDirectory()) {
            if(!dst.exists()){
                dst.mkdir();
//                System.out.println("Directory copied from "+ src + "  to " + dstDir);
            }
            for (String filePath : src.list()) {
                String src1 = (new File(src, filePath).getPath());
                String dst1 = dst.getPath();
                copyFileOrDirectory(src1, dst1);
            }
        } else {
            copyFile(src, dst);
        }
    }

    private static void copyFile(File sourceFile, File destFile) {
        if (!destFile.getParentFile().exists()) {
            if(destFile.getParentFile().mkdirs()) {
                Log.d(TAG,"create parent folder failed");
                return;
            }
        }

        if (!destFile.exists()) {
            try {
                if(destFile.createNewFile()) {
                    Log.d(TAG,"create new file failed");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());

            source.close();
            destination.close();
        } catch (Exception e) {
            e.printStackTrace();
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

    public static File getRecycleBin() {
        return getFile(LocalPathUtils.RECYCLE_BIN_DIR);
    }

    private static String getExtension(String name) {
        String extension = "";

        int i = name.lastIndexOf('.');
        if (i > 0) {
            extension = name.substring(i+1);
        }
        return extension;
    }
}
