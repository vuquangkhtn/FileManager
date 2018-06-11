package com.onlinetest.vuquang.filemanager.utils;

import android.util.Log;
import android.webkit.MimeTypeMap;

import com.onlinetest.vuquang.filemanager.app.LocalPathUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class FileHelper {
    private static final String TAG = "FileHelper";

    public static boolean isExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static boolean createDirectory(String path) {
        File file = new File(path);
        return file.mkdirs();
    }

    public static boolean createFile(String path) {
        boolean createFile = false;
        File file;
        if(MimeTypeMap.getFileExtensionFromUrl(path).isEmpty()) {
            file = new File(path+".txt");
        } else {
            file = new File(path);
        }
        try {
            createFile = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
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

    private static boolean copyFile(File sourceFile, File destFile) {
        if (!destFile.getParentFile().exists()) {
            if(!destFile.getParentFile().mkdirs()) {
                Log.d(TAG,"create parent folder failed");
                return false;
            } else {
                if (!destFile.exists()) {
                    try {
                        if(!destFile.createNewFile()) {
                            Log.d(TAG,"create new file failed");
                            return false;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        InputStream is = null;
        OutputStream os = null;
        boolean copyFile;
        try {
            is = new FileInputStream(sourceFile);
            os = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
            copyFile = true;
        } catch(Exception e) {
            copyFile = false;
        }
        return copyFile;
    }

    static boolean appendStrToFile(String path, String message) {
        if(!isExist(path))
        {
            createDirectory(getParentDir(path));
            if(!createFile(path)) {
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
        if(FileHelper.getParentDir(srcPath).equals(desDir)) {
            return false;
        }
        File file = new File(desDir);
        if(!file.exists() || file.isFile()) {
            return false;
        }
//        File desFile = new File(desDir + File.separator + FileHelper.getFileName(srcPath));
//        if(desFile.exists()) {
//            return false;
//        }
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
