package com.example.sqroom;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cars")
public class Car {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = "id")
    private long id;

    @ColumnInfo(name = "model")
    private String model;

    @ColumnInfo(name = "price")
    private String price;

    public Car(long id, String model, String price) {
        this.id = id;
        this.model = model;
        this.price = price;
    }

    @Ignore
    public Car(String model, String price) {
        this.model = model;
        this.price = price;
    }

    @Ignore
    public Car() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
