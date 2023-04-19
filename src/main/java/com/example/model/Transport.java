package com.example.model;

import com.example.annotation.FieldInformation;
import com.example.annotation.GetMethod;
import com.example.annotation.SetMethod;

import java.util.Objects;

public abstract class Transport {
    @FieldInformation(name = "Цвет", type = "TransportColor")
    private TransportColor color;
    @FieldInformation(name = "Количество колес", type = "Integer")
    private int amountOfWheels;
    @FieldInformation(name = "Количество мест", type = "Integer")
    private int amountOfSeats;
    @FieldInformation(name = "VIN", type = "String")
    private String vin;

    public Transport() {

    }

    public Transport(final TransportColor color, final int amountOfWheels, final int amountOfSeats, final String vin) {
        this.color = color;
        this.amountOfSeats = amountOfSeats;
        this.amountOfWheels = amountOfWheels;
        this.vin = vin;
    }

    @GetMethod(name = "VIN", returnType = "String")
    public String getVin() {
        return vin;
    }

    @GetMethod(name = "Цвет", returnType = "TransportColor")
    public TransportColor getColor() {
        return color;
    }

    @GetMethod(name = "Количество колес", returnType = "Integer")
    public int getAmountOfWheels() {
        return amountOfWheels;
    }

    @GetMethod(name = "Количество мест", returnType = "Integer")
    public int getAmountOfSeats() {
        return amountOfSeats;
    }

    @SetMethod(name = "VIN", typeParameter = "String")
    public void setVin(String vin) {
        this.vin = vin;
    }

    @SetMethod(name = "Цвет", typeParameter = "TransportColor")
    public void setColor(TransportColor color) {
        this.color = color;
    }

    @SetMethod(name = "Количество колес", typeParameter = "Integer")
    public void setAmountOfWheels(int amountOfWheels) {
        this.amountOfWheels = amountOfWheels;
    }

    @SetMethod(name = "Количество мест", typeParameter = "Integer")
    public void setAmountOfSeats(int amountOfSeats) {
        this.amountOfSeats = amountOfSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transport transport)) return false;
        return getAmountOfWheels() == transport.getAmountOfWheels() && getAmountOfSeats() == transport.getAmountOfSeats()
                && getColor() == transport.getColor() && getVin().equals(transport.getVin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColor(), getAmountOfWheels(), getAmountOfSeats(), getVin());
    }
}
