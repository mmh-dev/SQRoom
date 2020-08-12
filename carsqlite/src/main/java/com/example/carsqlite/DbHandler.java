package com.example.carsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    public DbHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CARS_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Util.KEY_MODEL + " TEXT,"
                + Util.KEY_PRICE + " TEXT" + ")";

        db.execSQL(CREATE_CARS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);
        onCreate(db);
    }

    public long addCar(Car c){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.KEY_MODEL, c.getModel());
        contentValues.put(Util.KEY_PRICE, c.getPrice());
        long i = db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();
        return i;
    }

    public Car getCar(long id){
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME,
                new String []{Util.KEY_ID, Util.KEY_MODEL, Util.KEY_PRICE},
                Util.KEY_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);
        Car c = new Car();
        if (cursor != null){
            try {
                cursor.moveToFirst(); // checking id there are any objects in Cursor
                c = new Car(Long.parseLong(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2));
            }finally {
                cursor.close();
            }

        }
        return c;
    }

    public List<Car> getAllCars (){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Car> carList = new ArrayList<>();

        String selectAllCars = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAllCars, null);
        Car c = new Car();
        if (cursor.moveToFirst()){
            try{
                do {
                    c.setId(Long.parseLong(cursor.getString(0)));
                    c.setModel(cursor.getString(1));
                    c.setPrice(cursor.getString(2));
                    carList.add(c);

                }while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }

        return carList;
    }

    public void updateCar(Car c){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.KEY_MODEL, c.getModel());
        contentValues.put(Util.KEY_PRICE, c.getPrice());

        db.update(Util.TABLE_NAME, contentValues, Util.KEY_ID + "=?",
                new String[]{String.valueOf(c.getId())});  // returns int id of the car selected
        db.close();
    }

    public void deleteCar (Car c){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?", new String[]{String.valueOf(c.getId())});
        db.close();
    }

    public void deleteAllCars (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Util.TABLE_NAME); // delete all content of the table
        db.close();
    }
}
