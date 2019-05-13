package com.yash.delhimetro.DataProviders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MetroFareDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "Metro";


    private static final String TABLE_FARE = "FareMetro";
    private static final String Fare = "Fare";
    private static final String FromStation = "FromStation";
    private static final String ToStation = "ToStation";
    private static final String FareQuery = "FareQuery";


    public MetroFareDBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) hexString.append(Integer.toHexString(0xFF & b));

            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_FARE_TABLE = "CREATE TABLE " + TABLE_FARE + "("
            + "" + FromStation + " VARCHAR(50), " + ToStation +
            " VARCHAR(50)," + Fare+" INTEGER"+
                " ,FareQuery VARCHAR(256) PRIMARY KEY)";
        db.execSQL(CREATE_FARE_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FARE);

        // Create tables again
        onCreate(db);
    }


    public void addAll(ArrayList<FareMetro> fareMetroArrayList){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            // add all

            for (FareMetro fm: fareMetroArrayList) {
                addFareMetro(fm);
            }

            db.setTransactionSuccessful();
        } catch(Exception e) {
            e.printStackTrace();
            //Error in between database transaction
        } finally {
            db.endTransaction();
        }

    }

    // code to add the new Fare Metro

    private void addFareMetro(FareMetro fareMetro) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FromStation, fareMetro.getFromStation()); // from station
        values.put(ToStation, fareMetro.getToStation()); // to station
        values.put(Fare, fareMetro.getFare()); // fare

        String hash = fareMetro.getFromStation()
                +"->"+fareMetro.getToStation();

        hash = md5(hash);

        values.put(FareQuery, hash);

        // Inserting Row
        db.insert(TABLE_FARE, null, values);
        //2nd argument is String containing nullColumnHack
        //        db.close();
    }


    // code to get the fare row
    public FareMetro getFareMetro(String From, String To) {
        SQLiteDatabase db = this.getReadableDatabase();

        String hash = From+"->"+To;

        hash = md5(hash);

        Cursor cursor = db.query(TABLE_FARE, new String[] {
                FromStation, ToStation, Fare },

                FareQuery + " =? ",
                new String[] { hash },

                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();


        // return fare metro
        FareMetro fareMetro = new FareMetro(
                cursor.getString(0),
                cursor.getString(1),
                Integer.parseInt(cursor.getString(2))
        );
        return fareMetro;

    }




}
