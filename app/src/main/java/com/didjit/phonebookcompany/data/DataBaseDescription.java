package com.didjit.phonebookcompany.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by didjit on 025 25.04.17.
 */

public class DataBaseDescription {
    public static final String AUTHORITY = "com.didjit.phonebookcompany.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);



    public static final class Contact implements BaseColumns {
        public static String TABLE_NAME = "contacts";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_POSITION = "position";
        public static final String COLUMN_DEPARTMENT = "department";
        public static final String COLUMN_PHONE = "phone";



        public static Uri buildContactUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);

        }


    }


}
