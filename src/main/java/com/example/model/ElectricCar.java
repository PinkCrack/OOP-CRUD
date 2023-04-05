package com.example.model;

import com.example.annotation.FieldInformation;
import com.example.annotation.GetMethod;
import com.example.annotation.SetMethod;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElectricCar that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getBattery(), that.getBattery());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getBattery());
    }
}
