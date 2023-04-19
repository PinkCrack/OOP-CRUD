package com.example.model;

import com.example.annotation.FieldInformation;
import com.example.annotation.GetMethod;
import com.example.annotation.SetMethod;

import java.util.Objects;

public class GasolineCar extends Car {
    @FieldInformation(name = "Вид бензина", type = "GasolineType")
    private GasolineType gasolineType;

    public GasolineCar(final TransportColor color, final int amountOfWheels, final int amountOfSeats, final String vin,
                       final CarBody typeOfBody, final GasolineType gasolineType) {
        super(color, amountOfWheels, amountOfSeats, vin, typeOfBody);
        this.gasolineType = gasolineType;
    }

    public GasolineCar() {
        super();
    }

    @GetMethod(name = "Вид бензина", returnType = "GasolineType")
    public GasolineType getGasolineType() {
        return gasolineType;
    }

    @SetMethod(name = "Вид бензина", typeParameter = "GasolineType")
    public void setGasolineType(GasolineType gasolineType) {
        this.gasolineType = gasolineType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GasolineCar that)) return false;
        if (!super.equals(o)) return false;
        return getGasolineType() == that.getGasolineType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getGasolineType());
    }
}
