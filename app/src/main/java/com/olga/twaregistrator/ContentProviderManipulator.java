package com.olga.twaregistrator;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by olga on 28/02/2016.
 */

public class ContentProviderManipulator {

    private final String TAG = ContentProviderManipulator.class.getSimpleName();
    ContentResolver resolver;
    public static final String ACTION_BROADCAST_NUMBER_RECEIVED = "com.olga.twaregistrator.number.received.broadcast";

    public void PutData2ContentProvider(Context context, String incomingNumber) {

        Log.d(TAG, "PutData2ContentProvider");

        resolver = context.getContentResolver();
        resolver.delete(RegisterDatabaseContract.CONTENT_URI, null, null);

        ContentValues initialValues = new ContentValues();
        initialValues.put(RegisterDatabaseContract.DB.COLUMN_NUMBER, incomingNumber);
        resolver.insert(RegisterDatabaseContract.CONTENT_URI, initialValues);
        Log.d(TAG, "PutData2ContentProvider done: " + incomingNumber + ":");

        sendSyncedBroadcast(context);
    }

    private void sendSyncedBroadcast(Context context) {
        Log.d(TAG, "sendSyncedBroadcast : " + ACTION_BROADCAST_NUMBER_RECEIVED + ":");
        Intent intent = new Intent(ACTION_BROADCAST_NUMBER_RECEIVED);
        intent.setType("*/*");
        context.sendBroadcast(intent);
        Log.d(TAG, "sendSyncedBroadcast done : " + ACTION_BROADCAST_NUMBER_RECEIVED + ":");
    }
}
