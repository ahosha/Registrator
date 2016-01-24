package com.olga.twaregistrator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by olga on 24/01/2016.
 */
public class RegistratorDatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE = "CREATE TABLE " + RegisterDatabaseContract.DB.TABLE_NAME + " ( " +
            RegisterDatabaseContract.DB._ID + " INTEGER PRIMARY KEY, " +
            RegisterDatabaseContract.DB.COLUMN_NUMBER + " text " +
            " ) ";
    public static final int DATABASE_VERSION = 1;

    public RegistratorDatabaseOpenHelper(Context context) {
        super(context, RegisterDatabaseContract.DB.DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + RegisterDatabaseContract.DB.TABLE_NAME);
        onCreate(db);
    }
}
