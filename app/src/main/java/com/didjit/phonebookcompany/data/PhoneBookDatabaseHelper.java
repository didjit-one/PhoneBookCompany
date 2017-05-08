package com.didjit.phonebookcompany.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import  com.didjit.phonebookcompany.data.DataBaseDescription.Contact;



/**
 * Created by didjit on 025 25.04.17.
 */


class PhoneBookDatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "PhoneBook.db";
        private static final int DATABASE_VERSION = 1;


        public PhoneBookDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            final String CREATE_CONTACTS_TABLE =
                    "CREATE TABLE " + Contact.TABLE_NAME + "(" +
                            Contact._ID + " integer primary key, " +
                            Contact.COLUMN_NAME + " TEXT, " +
                            Contact.COLUMN_PHONE + " TEXT, " +
                            Contact.COLUMN_POSITION + " TEXT, " +
                            Contact.COLUMN_DEPARTMENT + " TEXT);";
            db.execSQL(CREATE_CONTACTS_TABLE);
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) { }
    }
