package com.example.serialization;

import com.example.model.Transport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public interface Serializer {
    void serialize(File file, ArrayList<Transport> list);
    ArrayList<Transport> deserialize(File file) throws IOException, ClassNotFoundException;
}
