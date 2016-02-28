package com.olga.twaregistrator;

/**
 * Created by olga on 24/01/2016.
 */
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;


public class OugoingCallReceiver extends BroadcastReceiver
{

    //Context mContext;
    private final String TAG = OugoingCallReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "OugoingCallReceiver -> Outcoming");
        String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        ContentProviderManipulator manipulator = new ContentProviderManipulator();
        manipulator.PutData2ContentProvider(context, phoneNumber);

        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Log.d(TAG, "OugoingCallReceiver -> Received outgoing: " + phoneNumber + ":");


    }



}
