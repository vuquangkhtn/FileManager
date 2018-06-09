package com.onlinetest.vuquang.filemanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.onlinetest.vuquang.filemanager.utils.FLog;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            try {
//                BasicFileAttributes fileAttributes = Files.readAttributes(Paths.get("abc"),BasicFileAttributes.class);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        FLog.show("test");
    }
}
