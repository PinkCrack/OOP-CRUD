package com.example.serialization.factory;

import com.example.serialization.BinarySerializer;
import com.example.serialization.Serializer;

public class BinarySerializerFactory extends SerializerFactory {

    @Override
    public Serializer createSerializer() {
        return new BinarySerializer();
    }
}
