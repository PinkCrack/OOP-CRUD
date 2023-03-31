package com.example.factories;

import com.example.model.Bus;
import com.example.model.Transport;
import com.example.model.TransportColor;

public class BusFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Bus();
    }
}
