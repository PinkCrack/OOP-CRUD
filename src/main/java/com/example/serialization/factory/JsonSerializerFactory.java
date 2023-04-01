package com.example.serialization.factory;

import com.example.serialization.JsonSerializer;
import com.example.serialization.Serializer;

public class JsonSerializerFactory extends SerializerFactory {
    @Override
    public Serializer createSerializer() {
        return new JsonSerializer();
    }
}
