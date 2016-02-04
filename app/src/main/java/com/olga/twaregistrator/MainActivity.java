package com.olga.twaregistrator;

import android.app.DatePickerDialog;
import android.app.LoaderManager;
import android.app.TimePickerDialog;
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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
                            implements LoaderManager.LoaderCallbacks<Cursor>,
                            View.OnClickListener {

    private BroadcastReceiver receiver;
    private TextView numberdisplay;
    private static final int LOADER_ID = 1;
    SimpleCursorAdapter adapter;
    private final String TAG = MainActivity.class.getSimpleName();
    private String sitsAmount;

    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText fromDateEtxt = null;
    private EditText fromTimeEtxt = null;
    private TextView nameTW = null;
    private TimePickerDialog mTimePicker;

    private String bookingemail = "fedia.bar.seven@gmail.com";

    Button rbutton = null;
    Spinner spinner = null;
    ListView listView = null;

    @Override
    public void onClick(View view) {
        Log.d(TAG, "viewd: " + view);

        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        }
        else if(view == fromTimeEtxt) {
            mTimePicker.show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FindAllControls();

        PopulateActionButton();

        PopulateDateTimeDialog();

        PopulateSitsSpinner();

        PopulateNumberAdapter();

        //nameTW.requestFocus();
        //editText = (EditText)findViewById(R.id.myTextViewId);
        //nameTW.requestFocus();
        //nameTW.setInputType(0);
        nameTW.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (v == nameTW) {
                    if (hasFocus) {
                        Log.d(TAG, "nameTW hasFocus" );
                        // Open keyboard
                        ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(nameTW, InputMethodManager.SHOW_FORCED);
                    } else {
                        Log.d(TAG, "nameTW has NO Focus" );
                        // Close keyboard
                        ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(nameTW.getWindowToken(), 0);
                    }
                }
            }
        });

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);

        IntentFilter filter = IntentFilter.create(IncommingCallReceiver.ACTION_BROADCAST_NUMBER_RECEIVED, "*/*");
        receiver = new NumberReceivedBroadcastReceiver();
        registerReceiver(receiver, filter);
    }

    private void PopulateNumberAdapter() {
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null,
                new String[]{
                          RegisterDatabaseContract.DB.COLUMN_NUMBER
                },
                new int[]{
                        android.R.id.text1
                }, 0);

        listView.setAdapter(adapter);
    }

    private void PopulateDateTimeDialog()
    {
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        //fromDateEtxt.requestFocus();
        fromDateEtxt.setOnClickListener(this);
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

/*        int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = newCalendar.get(Calendar.MINUTE);*/
        fromTimeEtxt.setInputType(InputType.TYPE_NULL);
        //fromTimeEtxt.requestFocus();
        fromTimeEtxt.setOnClickListener(this);
        mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                                                        @Override
                                                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                                            fromTimeEtxt.setText( selectedHour + ":" + selectedMinute);
                                                        }          //}, hour, minute, true);
                                                    }, 21, 30, true);
        mTimePicker.setTitle("Select Time");


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

    private void PopulateActionButton ()
    {
        rbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (sitsAmount == null || sitsAmount.isEmpty()) {
                    sitsAmount = "1";
                }
                String choosenDate = fromDateEtxt.getText().toString();
                String choosenTime = fromTimeEtxt.getText().toString();
                Cursor mycursor = (Cursor) listView.getItemAtPosition(0);
                String number = mycursor.getString(1);
                String name= nameTW.getText().toString();



                String booking =
                            " Name:" + name +
                            " | Phone:" + number +
                            " | Date:" + choosenDate +
                            " | Time:" + choosenTime +
                            " | Seats:" + sitsAmount ;

                /*String booking = "<html><body>" +
                        "<a>Name: </a> <b>" + name + "</b> <br />" +
                        "<a>Phone: </a> <b>" + number + "</b> <br/>" +
                        "<a>Date: </a> <b>" + choosenDate + "</b> <br />" +
                        "<a>Time: </a> <b>" + choosenTime + "</b> <br />" +
                        "<a>Seats: </a> <b>" + sitsAmount + "</b> <br />" +
                        "</body>" +
                        "</html>" ;
                */

                String subject = "PHONE BOOKING: " + choosenDate + ", " + sitsAmount +" seats, " + name;

                if (!nameTW.getText().toString().isEmpty() && !nameTW.getText().toString().equals(""))
                    if (!number.isEmpty())
                        if (!sitsAmount.isEmpty()) {
                            Log.d(TAG, "Registration email to be send: " + booking);
                            sendEmail(bookingemail, subject, booking);
                            Log.d(TAG, "Registration email was send: " + booking);
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

    private void FindAllControls()
    {
        rbutton = (Button) findViewById(R.id.regbutton);
        spinner = (Spinner) findViewById(R.id.sits_spinner);
        listView = (ListView)findViewById(R.id.number_syncadapter_listview);
        fromDateEtxt = (EditText) findViewById(R.id.etxt_fromdate);
        fromTimeEtxt = (EditText) findViewById(R.id.etxt_fromtime);
        nameTW = (TextView) findViewById(R.id.nameTextView);
    }


}
