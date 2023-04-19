package com.example.serialization;

import com.example.model.Transport;

import java.io.*;
import java.util.ArrayList;

public class BinarySerializer implements Serializer {
    private final String extension = ".bin";
    @Override
    public void serialize(File file, ArrayList<Transport> listOfTransport) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(listOfTransport);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Transport> deserialize(final byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream is = new ObjectInputStream(in);
        Object object = is.readObject();
        is.close();
        in.close();

        return (ArrayList<Transport>) object;
    }

    @Override
    public String getExtension() {
        return extension;
    }
}
