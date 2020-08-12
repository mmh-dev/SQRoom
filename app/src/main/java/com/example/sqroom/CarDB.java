package com.example.sqroom;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Car.class}, version = 1)

public abstract class CarDB extends RoomDatabase {
    public abstract CarDao getCarDao();
}
