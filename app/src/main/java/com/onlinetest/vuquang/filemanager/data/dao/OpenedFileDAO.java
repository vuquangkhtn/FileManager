package com.onlinetest.vuquang.filemanager.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.onlinetest.vuquang.filemanager.data.model.file.OpenedFile;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VuQuang on 6/9/2018.
 */

public class OpenedFileDAO extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "FileManager.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "opened_file";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_LAST_OPEN_TIME = "last_opened_time";

    public OpenedFileDAO(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                MessageFormat.format("CREATE TABLE {0} ({1} INTEGER PRIMARY KEY, {2} TEXT, {3} LONG)",
                        TABLE_NAME, COLUMN_ID, COLUMN_PATH, COLUMN_LAST_OPEN_TIME)
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MessageFormat.format("DROP TABLE IF EXISTS {0}", TABLE_NAME));
        onCreate(db);
    }

    public boolean insertOpenedFile(OpenedFile file) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_PATH, file.getPath());
        contentValues.put(COLUMN_LAST_OPEN_TIME, file.getLastOpenedTime());

        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Integer deleteOpenedFile(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteOpenedFile(String path) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                COLUMN_PATH + " = ? ",
                new String[] { path });
    }

    public OpenedFile getOpenedFile(int id) {
        OpenedFile file = new OpenedFile();

        SQLiteDatabase db = this.getReadableDatabase();
        String sqlExec = MessageFormat.format("SELECT * FROM {0} WHERE {1} = ?",TABLE_NAME, COLUMN_ID);
        Cursor cursor = db.rawQuery(sqlExec, new String[]{Integer.toString(id)});

        if (cursor != null)
            cursor.moveToFirst();

        file = new OpenedFile();
        file.setId(Integer.parseInt(cursor.getString(0)));
        file.setPath(cursor.getString(1));
        file.setLastOpenedTime(cursor.getLong(2));

        return file;
    }

    public List<OpenedFile> getAllOpenedFiles() {
        List<OpenedFile> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + TABLE_NAME, null );

        if (cursor.moveToFirst()) {
            do {
                OpenedFile file = new OpenedFile();
                file.setId(Integer.parseInt(cursor.getString(0)));
                file.setPath(cursor.getString(1));
                file.setLastOpenedTime(cursor.getLong(2));

                list.add(file);
            } while (cursor.moveToNext());
        }

        return list;
    }
}