package com.example.factories;

import com.example.model.Bike;
import com.example.model.Transport;
import javafx.scene.control.Control;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class BikeFactory extends TransportFactory {
    @Override
    public Transport createTransport() {
        return new Bike();
    }
}
