package com.daseyffert.timeblock.ApplicationTabs.Tab_List.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.daseyffert.timeblock.ApplicationTabs.Tab_List.NotesItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Daniel on 1/11/2016.
 */
public class NoteDbSchema extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "TimeBlockDB.db";
    public static final String TABLE_NAME = "ToDoList";
    public static final String COL_ID = "ID";
    public static final String COL_DESCRIPTION = "DESCRIPTION";
    public static final String COL_DATE = "DATE";

    public NoteDbSchema(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //Creates table within the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INT PRIMARY KEY, " +
                "DESCRIPTION TEXT, DATE DATE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Inserts new task into the database
    //Returns false if insertion is unsuccessful
    public boolean insertTask(NotesItem mNotesItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, mNotesItem.getId().toString());
        contentValues.put(COL_DESCRIPTION, mNotesItem.getDescription());
        contentValues.put(COL_DATE, formattedDate(mNotesItem.getDate()));

        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        if(result == -1)
            return false;
        else {
            //Log.e("DATABASE OPERATION", "New user added");
            return true;
        }
    }

    //returns all data stored in the database
    public Cursor storedTasks(SQLiteDatabase db) {
        Cursor cursor;
        String query = "Select * FROM " + TABLE_NAME;

        cursor = db.rawQuery(query, null);

        return cursor;
    }

    //Deletes task from database
    //Returns false if deletion is unsuccessful
    public boolean deleteTask(NotesItem mNotesItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        //delete method from SQLite class
        boolean result = db.delete(TABLE_NAME, COL_ID + " = ?",
                new String[] { String.valueOf(mNotesItem.getId())}) > 0;

        db.close();
        return result;
    }

    //Format Date to dd/mm/yyyy from whatever date given
    //Copied from NoteHolder class
    private String formattedDate(Date date) {
//            if (date == null)
//                return null;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String formatD = format.format(date);
        return formatD;
    }

}
