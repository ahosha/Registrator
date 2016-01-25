package com.olga.twaregistrator;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
                            implements LoaderManager.LoaderCallbacks<Cursor>{

    private BroadcastReceiver receiver;
    private TextView numberdisplay;
    private static final int LOADER_ID = 1;
    SimpleCursorAdapter adapter;
    private final String TAG = MainActivity.class.getSimpleName();
    private String sitsAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PopulateSitsSpinner();

        PopulateActionButton();


        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null,
                new String[]{
                          RegisterDatabaseContract.DB.COLUMN_NUMBER
                },
                new int[]{
                        android.R.id.text1
                }, 0);

        ListView listView = (ListView)findViewById(R.id.number_syncadapter_listview);
        listView.setAdapter(adapter);


        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);

        IntentFilter filter = IntentFilter.create(IncommingCallReceiver.ACTION_BROADCAST_NUMBER_RECEIVED, "*/*");
        receiver = new NumberReceivedBroadcastReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader id:" + String.valueOf(id));
        return new CursorLoader(this, RegisterDatabaseContract.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished " + data.getCount());
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset " );
        adapter.swapCursor(null);
    }


    protected void onResume() {
        super.onResume();

        IntentFilter filter = IntentFilter.create(IncommingCallReceiver.ACTION_BROADCAST_NUMBER_RECEIVED, "*/*");
        receiver = new NumberReceivedBroadcastReceiver();

        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public class NumberReceivedBroadcastReceiver extends BroadcastReceiver{
        private final String TAG = NumberReceivedBroadcastReceiver.class.getSimpleName();
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "NumberReceivedBroadcastReceiver onReceive");
            getLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
        }
    }

    private void PopulateSitsSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.sits_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sitslistArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                sitsAmount = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }

        });
    }

    private void PopulateActionButton() {
        Button fab = (Button) findViewById(R.id.regbutton);
        fab.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {


                TextView nameTW = (TextView) findViewById(R.id.nameTextView);
                if (sitsAmount == null || sitsAmount.isEmpty()) {
                    sitsAmount = "1";
                }

                EditText bookingDate = (EditText) findViewById(R.id.dateText);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String choosenDate = bookingDate.getText().toString();
                EditText bookingTime = (EditText) findViewById(R.id.timeText);
                String choosenTime = bookingTime.getText().toString();
                ListView receivednumber = (ListView) findViewById(R.id.number_syncadapter_listview);


                Cursor mycursor = (Cursor) receivednumber.getItemAtPosition(0);
                String number =  mycursor.getString(1);

                String booking = "Name: " + nameTW.getText().toString() +
                        "   Phone number: " + number +
                        "   Number of sits: " + sitsAmount +
                        "   Booking date: " + choosenDate +
                        "   Booking time: " + choosenTime + ";";

                String bookingemail = "maxim.langman@gmail.com";
                if (!nameTW.getText().toString().isEmpty() && !nameTW.getText().toString().equals(""))
                    if (!number.isEmpty())
                        if (!sitsAmount.isEmpty()) {
                            Log.d(TAG, "Registration email to be send: " + booking);
                            sendEmail(bookingemail, "Booking details", booking);
                            Log.d(TAG, "Registration email was send: " + booking);

                            //Toast.makeText(MainActivity.,  "Registration email was send: " + booking, Toast.LENGTH_LONG).show();
                        }

            }
        });
    }


    private void sendEmail(String email, String subject, String body) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email ..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }
}
