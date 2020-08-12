package com.example.carsqlite;

import androidx.annotation.NonNull;

public class Car {

    private long id;
    private String model;
    private String price;

    public Car(long id, String model, String price) {
        this.id = id;
        this.model = model;
        this.price = price;
    }

    public Car(String model, String price) {
        this.model = model;
        this.price = price;
    }

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
