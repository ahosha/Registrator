package com.olga.twaregistrator;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by olga on 24/01/2016.
 */
public class RegisterContractProvider  extends ContentProvider {

    private final String TAG = RegisterContractProvider.class.getSimpleName();
    private RegistratorDatabaseOpenHelper dbHelper;

    public RegisterContractProvider() {
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");
        dbHelper = new RegistratorDatabaseOpenHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        Log.d(TAG, "getType:" + uri + ":");
        return "vnd.android.cursor.dir/vnd.com.olga.twaregistrator.registercontract.provider";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert: " + uri + ":");
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newId = db.insert(RegisterDatabaseContract.DB.TABLE_NAME, null, values);

        Uri itemUri = uri.buildUpon().appendPath(Long.toString(newId)).build();
        getContext().getContentResolver().notifyChange(itemUri, null);
        return itemUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete uri"  + uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.delete(RegisterDatabaseContract.DB.TABLE_NAME,null, null);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: " + uri + ":");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor query = db.query(RegisterDatabaseContract.DB.TABLE_NAME, null, null, null, null, null, null);
        Log.d(TAG, "query: count:  " + query.getCount() + ":");
        return query;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(TAG, "update");
        throw new UnsupportedOperationException("Not implemented");
    }
}
