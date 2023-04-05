package com.example.model;

import com.example.annotation.FieldInformation;
import com.example.annotation.GetMethod;
import com.example.annotation.SetMethod;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bus bus)) return false;
        if (!super.equals(o)) return false;
        return getNumberOfBus() == bus.getNumberOfBus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNumberOfBus());
    }
}
