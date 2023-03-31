package com.example.model;

import com.example.annotation.FieldInformation;
import com.example.annotation.GetMethod;
import com.example.annotation.SetMethod;

public class Bus extends Transport {
    @FieldInformation(name = "Номер автобуса", type = "Integer")
    private int numberOfBus;

    public Bus() {

    }

    public Bus(final TransportColor color, final int amountOfWheels, final int amountOfSeats, final String vin, final int numberOfBus) {
        super(color, amountOfWheels, amountOfSeats, vin);
        this.numberOfBus = numberOfBus;
    }

    @GetMethod(name = "Номер автобуса", returnType = "Integer")
    public int getNumberOfBus() {
        return numberOfBus;
    }

    @SetMethod(name = "Номер автобуса", typeParameter = "Integer")
    public void setNumberOfBus(int numberOfBus) {
        this.numberOfBus = numberOfBus;
    }
}
