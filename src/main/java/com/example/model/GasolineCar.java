package com.example.model;

import com.example.annotation.FieldInformation;
import com.example.annotation.GetMethod;
import com.example.annotation.SetMethod;

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
    public GasolineType getPetrolType() {
        return gasolineType;
    }

    @SetMethod(name = "Вид бензина", typeParameter = "GasolineType")
    public void setPetrolType(GasolineType gasolineType) {
        this.gasolineType = gasolineType;
    }
}
