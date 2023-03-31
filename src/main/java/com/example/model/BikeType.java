package com.example.model;

import com.example.annotation.EnumInformation;

public enum BikeType {
    @EnumInformation(name = "Горный")
    MOUNTAIN,
    @EnumInformation(name = "Электровелосипед")
    ELECTRIC,
    @EnumInformation(name = "Дорожный")
    ROAD,
    @EnumInformation(name = "BMX")
    BMX,
    @EnumInformation(name = "Шоссейный")
    RACING
}
