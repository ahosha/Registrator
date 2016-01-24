package com.olga.twaregistrator;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by olga on 24/01/2016.
 */
public class RegisterDatabaseContract {

    public static final String AUTHORITY = "com.olga.twaregistrator.registercontract.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class DB implements BaseColumns {
        public static final String DATABASE_NAME = "registrator.db";
        public static final String TABLE_NAME = "calls";
        public static final String COLUMN_NUMBER = "number";
    }
}
