package com.example.serialization;

import com.example.model.*;
import com.example.serialization.serializationModel.TransportSerializeModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class JsonSerializer extends Serializer {

    private final ObjectMapper objectMapper;
    private final ObjectWriter objectWriter;
    private final TypeReference<ArrayList<Transport>> type = new TypeReference<ArrayList<Transport>>() {};
    public JsonSerializer()  {
        objectMapper = new ObjectMapper();
        objectWriter = objectMapper.writerFor(type);
    }

    @Override
    public void serialize(File file, ArrayList<Transport> listOfTransport) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            String json = objectWriter.writeValueAsString(listOfTransport);
            bufferedWriter.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Transport> deserialize(File file) throws IOException {
        ArrayList<Transport> listOfTransport;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String json = bufferedReader.lines().collect(Collectors.joining());
        listOfTransport = objectMapper.readValue(json, type);
        bufferedReader.close();

        return listOfTransport;
    }
}
