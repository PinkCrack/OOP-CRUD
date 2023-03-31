package com.example.model;

import com.example.annotation.FieldInformation;
import com.example.annotation.GetMethod;
import com.example.annotation.SetMethod;

public class ElectricCar extends Car {
    @FieldInformation(name = "Батарея", type = "Battery")
    private Battery battery;

    public ElectricCar(final TransportColor color, final int amountOfWheels, final int amountOfSeats, final String vin,
                       final CarBody typeOfBody, final Battery battery) {

        super(color, amountOfWheels, amountOfSeats, vin, typeOfBody);
        this.battery = battery;
    }

    public ElectricCar() {
        super();
    }

    @GetMethod(name = "Батарея", returnType = "Battery")
    public Battery getBattery() {
        return battery;
    }

    @SetMethod(name = "Батарея", typeParameter = "Battery")
    public void setBattery(Battery battery) {
        this.battery = battery;
    }
}
