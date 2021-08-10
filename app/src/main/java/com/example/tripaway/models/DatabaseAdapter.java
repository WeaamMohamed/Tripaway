package com.example.tripaway.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.w3c.dom.Text;

import java.sql.Date;
import java.sql.Time;
import java.util.Collections;

public class DatabaseAdapter {
    private static Helper dbHelper;
    private Context context;

    public DatabaseAdapter(Context context) {
        this.context = context;
        dbHelper = new Helper(context);
    }

    public long insertTrip(String tripName,String time, String date, String notes) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Helper.TRIP_NAME, tripName);
        cv.put(Helper.TIME.toString(), time);
        cv.put(Helper.DATE.toString(),date);
        cv.put(String.valueOf(Helper.NOTES), notes);
        long id = db.insert(Helper.TABLE_NAME, null, cv);
        return id;
    }

    public UpcomingTripModel[] getAllTrips() {
        UpcomingTripModel[] trips = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int i = 0;
        Cursor c;
        String[] columns = {Helper.UID, Helper.TRIP_NAME, String.valueOf(Helper.TIME), String.valueOf(Helper.DATE), String.valueOf(Helper.NOTES)};
        c = db.query(Helper.TABLE_NAME, columns, null, null, null, null, null);
        trips = new UpcomingTripModel[c.getCount()];
        while (c.moveToNext()) {
            trips[i] = new UpcomingTripModel(c.getString(0),c.getString(1),c.getString(2), Collections.singletonList(c.getString(3)));
            i++;
        }
        return trips;
    }


    public void deleteAllTrips() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from " + Helper.TABLE_NAME);
    }


    private static class Helper extends SQLiteOpenHelper {
        static final String DATABASE_NAME = "mydb";
        static final int DATABASE_VERSION = 1;
        static final String TABLE_NAME = "UPCOMINGTABLE";
        static final String UID = "_id";
        static final String TRIP_NAME = "trip_name";
        static final Date DATE = Date.valueOf("01 January 2022");
        static final Time TIME = Time.valueOf("00:00:00");
        static final Text NOTES = null;
        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE + " Date, " + TIME + " Time," + TIME + " varchar(25)," + NOTES + " Text;";

        public Helper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            //onCreate(db);
        }
    }
}
