package com.example.serialization;

import com.example.model.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class JsonSerializer implements Serializer {
    private final Type collectionType = new TypeToken<ArrayList<Transport>>() {
    }.getType();
    private final Gson gson;

    public JsonSerializer()  {
        RuntimeTypeAdapterFactory<Transport> vehicleAdapterFactory = RuntimeTypeAdapterFactory.of(Transport.class, "type")
                .registerSubtype(Bike.class, "Bike")
                .registerSubtype(Bus.class, "Bus")
                .registerSubtype(GasolineCar.class, "GasolineCar")
                .registerSubtype(ElectricCar.class, "ElectricCar");

        gson = new GsonBuilder().registerTypeAdapterFactory(vehicleAdapterFactory).create();
    }

    @Override
    public void serialize(File file, ArrayList<Transport> listOfTransport) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            String json = gson.toJson(listOfTransport, collectionType);
            bufferedWriter.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Transport> deserialize(File file) {
        ArrayList<Transport> listOfTransport;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String json = bufferedReader.lines().collect(Collectors.joining());
            listOfTransport = gson.fromJson(json, collectionType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return listOfTransport;
    }
}
