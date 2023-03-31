package com.example.factories;

import com.example.model.GasolineCar;
import com.example.model.Transport;

public class GasolineCarFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new GasolineCar();
    }
}
