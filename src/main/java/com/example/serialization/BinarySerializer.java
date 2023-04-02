package com.example.serialization;

import com.example.model.Transport;

import java.io.*;
import java.util.ArrayList;

public class BinarySerializer implements Serializer {
    @Override
    public void serialize(File file, ArrayList<Transport> listOfTransport) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(listOfTransport);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Transport> deserialize(File file) throws IOException, ClassNotFoundException {
        ArrayList<Transport> listOfTransport;
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
        listOfTransport = (ArrayList<Transport>) objectInputStream.readObject();
        objectInputStream.close();

        return listOfTransport;
    }

}
