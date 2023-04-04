package com.example.factoryMethod.serialization;

import com.example.serialization.TextSerializer;
import com.example.serialization.Serializer;

public class TextSerializerFactory implements SerializerFactory {
    @Override
    public Serializer createSerializer() {
        return new TextSerializer();
    }
}
