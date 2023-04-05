package com.example.model;

import com.example.annotation.FieldInformation;
import com.example.annotation.GetMethod;
import com.example.annotation.SetMethod;

import java.util.Objects;

public abstract class Car extends Transport {
    @FieldInformation(name = "Тип кузова", type = "CarBody")
    private CarBody typeOfBody;

    public Car() {

    }

    public Car(final TransportColor color, final int amountOfWheels, final int amountOfSeats, final String vin, final CarBody typeOfBody) {
        super(color, amountOfWheels, amountOfSeats, vin);
        this.typeOfBody = typeOfBody;
    }

    @GetMethod(name = "Тип кузова", returnType = "CarBody")
    public CarBody getTypeOfBody() {
        return typeOfBody;
    }

    @SetMethod(name = "Тип кузова", typeParameter = "CarBody")
    public void setTypeOfBody(CarBody typeOfBody) {
        this.typeOfBody = typeOfBody;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car car)) return false;
        if (!super.equals(o)) return false;
        return getTypeOfBody() == car.getTypeOfBody();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTypeOfBody());
    }
}
