package com.example.factoryMethod.serialization;

import com.example.serialization.JsonSerializer;
import com.example.serialization.Serializer;

public class JsonSerializerFactory implements SerializerFactory {
    @Override
    public Serializer createSerializer() {
        return new JsonSerializer();
    }
}
