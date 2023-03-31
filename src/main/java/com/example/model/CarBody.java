package com.example.model;

import com.example.annotation.EnumInformation;

public enum CarBody {
    @EnumInformation(name = "Седан")
    SEDAN,
    @EnumInformation(name = "Хэтчбек")
    HATCHBACK,
    @EnumInformation(name = "Купе")
    COUPE,
    @EnumInformation(name = "Кроссовер")
    CROSSOVER
}
