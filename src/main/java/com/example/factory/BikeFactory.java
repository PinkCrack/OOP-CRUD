package com.example.factory;

import com.example.model.Bike;
import com.example.model.Transport;

public class BikeFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Bike();
    }
}
