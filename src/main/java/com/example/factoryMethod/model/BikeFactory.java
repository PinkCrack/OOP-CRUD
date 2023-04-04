package com.example.factoryMethod.model;

import com.example.model.Bike;
import com.example.model.Transport;

public class BikeFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Bike();
    }
}
