package com.example.model;

import com.example.annotation.EnumInformation;

public enum TransportColor {
    @EnumInformation(name = "Красный")
    RED,
    @EnumInformation(name = "Черный")
    BLACK,
    @EnumInformation(name = "Синий")
    BLUE,
    @EnumInformation(name = "Белый")
    WHITE,
    @EnumInformation(name = "Зеленый")
    GREEN
}
