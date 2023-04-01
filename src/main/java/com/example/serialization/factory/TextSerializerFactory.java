package com.example.serialization.factory;

import com.example.serialization.TextSerializer;
import com.example.serialization.Serializer;

public class TextSerializerFactory extends SerializerFactory {
    @Override
    public Serializer createSerializer() {
        return new TextSerializer();
    }
}
