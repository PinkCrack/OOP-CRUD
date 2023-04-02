package com.example.serialization;

import com.example.model.Transport;
import com.example.serialization.serializationModel.*;
import com.example.serialization.serializationModel.factory.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Serializer {
    public abstract void serialize(File file, ArrayList<Transport> list);
    public abstract ArrayList<Transport> deserialize(File file) throws IOException, ClassNotFoundException;

    public Serializer() {
//        transportSerializeModelMap.put("Bike", new BikeSerializeFactory());
//        transportSerializeModelMap.put("Bus", new BusSerializeFactory());
//        transportSerializeModelMap.put("ElectricCar", new ElectricCarSerializeFactory());
//        transportSerializeModelMap.put("GasolineCar", new GasolineCarSerializeFactory());
    }

//    private final Map<String, TransportSerializeFactory> transportSerializeModelMap = new HashMap<>();

//    private ArrayList<TransportSerializeModel> getSerializeModelList(ArrayList<Transport> list) {
//        ArrayList<TransportSerializeModel> serializeModels = new ArrayList<>();
//        for (Transport transport : list) {
//            TransportSerializeFactory transportSerializeFactory = transportSerializeModelMap.get(transport.getClass().getSimpleName());
//            serializeModels.add(transportSerializeFactory.createTransportSerializationModel(transport));
//        }
//        return serializeModels;
//    }
}
