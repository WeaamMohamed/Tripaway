//package com.example.tripaway.models;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DatabaseAdapter {
//    private static Helper dbHelper;
//    private Context context;
//
//    public DatabaseAdapter(Context context) {
//        this.context = context;
//        dbHelper = new Helper(context);
//    }
//
//    public long insertTrip(String tripName,String date,String time) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//        cv.put(Helper.TRIP_NAME, tripName);
//        cv.put(Helper.DATE, date);
//        cv.put(Helper.TIME, time);
//        long id = db.insert(Helper.TABLE_NAME, null, cv);
//        return id;
//    }
//
//    public UpcomingTripModel[] getAllTrips() {
//        UpcomingTripModel[] trips = null;
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        int i = 0;
//        Cursor c;
//        String[] columns = {Helper.TRIP_NAME, Helper.DATE ,Helper.TIME};
//        c = db.query(Helper.TABLE_NAME, columns, null, null, null, null, null);
//        trips = new UpcomingTripModel[c.getCount()];
//        while (c.moveToNext()) {
//            trips[i] = new UpcomingTripModel(c.getString(0),c.getString(1),c.getString(2));
//            i++;
//        }
//        return trips;
//    }
//
//
//    public void deleteAllTrips() {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.execSQL("delete from " + Helper.TABLE_NAME);
//    }
////    public String[] getTipDT() {
////
////        final String TABLE_NAME = "UPCOMINGTABLE";
////
////        String selectQuery = "SELECT  DATE_TIME FROM  UPCOMINGTABLE";
////        SQLiteDatabase db  = dbHelper.getReadableDatabase();
////        Cursor cursor      = db.rawQuery(selectQuery, null);
////        String[] data      = new String[cursor.getCount()];
////        int c = cursor.getCount();
////        int i = 0;
////        while (i < c){
////
////            data[i] = cursor.getString(0);
////            Log.i("dttt", " "+cursor.getString(0));
////            i++;
////        }
////        cursor.close();
////        return data;
////    }
//
//
//    private static class Helper extends SQLiteOpenHelper {
//        static final String DATABASE_NAME = "mydb";
//        static final int DATABASE_VERSION = 1;
//        static final String TABLE_NAME = "UPCOMINGTABLE";
//        static final String UID = "_id";
//        static final String TRIP_NAME = "trip_name";
//        static final String DATE = "date";
//        static final String TIME = "time" ;
//        static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
//                " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TRIP_NAME + " varchar(25), " + DATE + " varchar(25), " + TIME + " varchar(25)); ";
//
//        public Helper(Context context) {
//            super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            db.execSQL(CREATE_TABLE);
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//            //onCreate(db);
//        }
//    }
//}
