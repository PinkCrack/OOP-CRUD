package com.example.factoryMethod.serialization;

import com.example.serialization.Serializer;

public interface SerializerFactory {
    Serializer createSerializer();
}
