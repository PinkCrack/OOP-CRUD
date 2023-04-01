package com.example.serialization.factory;

import com.example.serialization.Serializer;

public abstract class SerializerFactory {
    public abstract Serializer createSerializer();
}
