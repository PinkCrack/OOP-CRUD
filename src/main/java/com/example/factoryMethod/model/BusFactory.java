package com.example.factoryMethod.model;

import com.example.model.Bus;
import com.example.model.Transport;

public class BusFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Bus();
    }
}
