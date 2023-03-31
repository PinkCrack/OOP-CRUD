package com.example.model;

import com.example.annotation.EnumInformation;

public enum GasolineType {
    @EnumInformation(name = "АИ-87")
    REGULAR87,
    @EnumInformation(name = "АИ-95")
    PREMIUM95,
    @EnumInformation(name = "АИ-98")
    PREMIUM98
}
