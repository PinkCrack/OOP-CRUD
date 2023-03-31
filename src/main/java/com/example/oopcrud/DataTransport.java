package com.example.oopcrud;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DataTransport {
    private final SimpleIntegerProperty number;
    private final SimpleStringProperty type;
    private final SimpleStringProperty vin;

    public DataTransport(int number, String type, String vin) {
        this.number = new SimpleIntegerProperty(number);
        this.type = new SimpleStringProperty(type);
        this.vin = new SimpleStringProperty(vin);
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public void setVin(String vin) {
        this.vin.set(vin);
    }

    public int getNumber() {
        return number.get();
    }

    public String getType() {
        return type.get();
    }

    public String getVin() {
        return vin.get();
    }
}
