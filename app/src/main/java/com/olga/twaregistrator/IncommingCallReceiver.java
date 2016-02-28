package com.olga.twaregistrator;

/**
 * Created by olga on 24/01/2016.
 */
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.olga.twaregistrator.RegisterDatabaseContract;

import java.io.File;



public class IncommingCallReceiver extends BroadcastReceiver
{

    //Context mContext;
    private final String TAG = IncommingCallReceiver.class.getSimpleName();

    /*
    //final String FileName = "ReceivedNumber.txt";
    ContentResolver resolver;

    public static final String ACTION_BROADCAST_NUMBER_RECEIVED = "com.olga.twaregistrator.number.received.broadcast";*/



    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "IncommingCallReceiver -> Received Incoming");
        //Toast.makeText(mContext, "IncommingCallReceiver -> onReceive", Toast.LENGTH_LONG).show();
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String msg = "Phone state changed to " + state;

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            msg += ". Incoming number is " + incomingNumber;

            ContentProviderManipulator manipulator = new ContentProviderManipulator();
            manipulator.PutData2ContentProvider(context, incomingNumber);

        }
/*        else
        {
            Log.d(TAG, "OugoingCallReceiver -> Outcoming");

            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            if (!TextUtils.isEmpty(phoneNumber) ) {
                Log.d(TAG, "OugoingCallReceiver -> Outcoming: " + phoneNumber);

                ContentProviderManipulator manipulator = new ContentProviderManipulator();
                manipulator.PutData2ContentProvider(context, phoneNumber);

                msg += ". Outgoing number is " + phoneNumber;
            }
        }*/

        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Log.d(TAG, "IncommingCallReceiver -> Received Incoming: " + msg + ":");


    }

/*

    private void PutData2ContentProvider(Context context, String incomingNumber) {

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
        intent.setType("**

/*");
        context.sendBroadcast(intent);
        Log.d(TAG, "sendSyncedBroadcast done : " + ACTION_BROADCAST_NUMBER_RECEIVED + ":");
    }
*/







}
