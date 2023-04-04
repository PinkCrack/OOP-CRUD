package com.example.serialization;

import com.example.model.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TextSerializerTest {

    private ArrayList<Transport> listOfTransport = new ArrayList<>();
    private TextSerializer textSerializer;
    private String text = "Класс:Bike;Тип велосипеда:BMX;Диаметр колес:33.0;Количество колес:2;Количество мест:1;VIN:JSDF323;Цвет:КрасныйКласс:Bus;Номер автобуса:303;Количество колес:6;Количество мест:22;VIN:JSDFS322ASD23;Цвет:ЧерныйКласс:ElectricCar;Батарея:[Класс:Battery;Потребление энергии:120;Объем:330];Тип кузова:Седан;Количество колес:4;Количество мест:5;VIN:TESLA323;Цвет:ЗеленыйКласс:GasolineCar;Вид бензина:АИ-98;Тип кузова:Хэтчбек;Количество колес:4;Количество мест:4;VIN:TATO32DDD323;Цвет:Белый";
    private String fileName = "C:\\Users\\Nikita\\IdeaProjects\\OOP-CRUD\\src\\test\\java\\com\\example\\serialization\\test.txt";

    TextSerializerTest() {
        textSerializer = new TextSerializer();
        listOfTransport.add(new Bike(TransportColor.RED, 2,1, "JSDF323", 33, BikeType.BMX));
        listOfTransport.add(new Bus(TransportColor.BLACK, 6,22, "JSDFS322ASD23", 303));
        listOfTransport.add(new ElectricCar(TransportColor.GREEN, 4,5, "TESLA323", CarBody.SEDAN, new Battery(120,330)));
        listOfTransport.add(new GasolineCar(TransportColor.WHITE, 4,4, "TATO32DDD323", CarBody.HATCHBACK, GasolineType.PREMIUM98));
    }

    @Test
    void serializeTest() {
        File file = new File(fileName);
        if (file != null) {
//            textSerializer.serialize(listOfTransport);
//            StringBuilder textFile = new StringBuilder();
//            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
//                while (bufferedReader.ready()) {
//                    textFile.append(bufferedReader.readLine());
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            assertEquals(textFile.length(), text.length());
        }
    }

    @Test
    void deserializeTest() throws IOException {
//        File file = new File(fileName);
//        if (file != null) {
//            ArrayList<Transport> listFromFile = textSerializer.deserialize(file);
//            for (int i = 0; i < listOfTransport.size(); i++) {
//                assertEquals(listFromFile.get(i).getVin(), listOfTransport.get(i).getVin());
//            }
//            assertEquals(listFromFile.size(), listOfTransport.size());
//        }
    }
}