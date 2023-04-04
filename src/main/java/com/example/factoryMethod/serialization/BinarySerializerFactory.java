package com.example.factoryMethod.serialization;

import com.example.serialization.BinarySerializer;
import com.example.serialization.Serializer;

public class BinarySerializerFactory implements SerializerFactory {
    @Override
    public Serializer createSerializer() {
        return new BinarySerializer();
    }
}
