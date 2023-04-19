package com.example.model;

import com.example.annotation.FieldInformation;
import com.example.annotation.GetMethod;
import com.example.annotation.SetMethod;

import java.util.Objects;

public class Battery {
    @FieldInformation(name = "Потребление энергии", type = "Integer")
    private int energyConsumption;

    @FieldInformation(name = "Объем", type = "Integer")
    private int capacity;

    public Battery() {

    }

    public Battery(final int energyConsumption, final int capacity) {
        this.energyConsumption = energyConsumption;
        this.capacity = capacity;
    }

    @GetMethod(name = "Потребление энергии", returnType = "Integer")
    public int getEnergyConsumption() {
        return energyConsumption;
    }

    @GetMethod(name = "Объем", returnType = "Integer")
    public int getCapacity() {
        return capacity;
    }

    @SetMethod(name = "Потребление энергии", typeParameter = "Integer")
    public void setEnergyConsumption(int energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    @SetMethod(name = "Объем", typeParameter = "Integer")
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Battery battery)) return false;
        return getEnergyConsumption() == battery.getEnergyConsumption() && getCapacity() == battery.getCapacity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEnergyConsumption(), getCapacity());
    }
}
