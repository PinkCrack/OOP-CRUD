package com.example.serialization;

import com.example.annotation.EnumInformation;
import com.example.annotation.GetMethod;
import com.example.annotation.SetMethod;
import com.example.factories.*;
import com.example.model.Transport;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class TextSerializer implements Serializer {

    private final String BIKE = "Bike";
    private final String BUS = "Bus";
    private final String ELECTRIC_CAR = "ElectricCar";
    private final String GASOLINE_CAR = "GasolineCar";
    private final String BATTERY = "Battery";
    private final HashMap<String, TransportFactory> transportFactoryMap = new HashMap<>();

    public TextSerializer() {
        transportFactoryMap.put(BIKE, new BikeFactory());
        transportFactoryMap.put(BUS, new BusFactory());
        transportFactoryMap.put(ELECTRIC_CAR, new ElectricCarFactory());
        transportFactoryMap.put(GASOLINE_CAR, new GasolineCarFactory());
    }

    @Override
    public void serialize(File file, ArrayList<Transport> list) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(listToText(list));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Transport> deserialize(File file) {
        ArrayList<Transport> list = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            while (bufferedReader.ready()) {
                list.add((Transport) textToObject(bufferedReader.readLine()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    private String listToText(ArrayList<Transport> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Transport transport : list) {
            try {
                stringBuilder.append(valuesToString(transport.getClass(), transport));
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    private Object textToObject(String str) {
        HashMap<String, String> valuesMap = textToMap(str);

        TransportFactory transportFactory = transportFactoryMap.get(valuesMap.get("Класс"));
        Object object = transportFactory.createTransport();
        try {
            setValuesToObject(object.getClass(), object, valuesMap);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }

        return object;
    }

    private HashMap<String, String> textToMap(String str) {
        HashMap<String, String> valuesMap = new HashMap<>();
        char[] chars = str.toCharArray();
        String name = "";
        String value = "";
        int nameIndex = 0;
        int valueIndex = 0;
        boolean isInnerObject = false;
        for (int i = 0; i < chars.length + 1; i++) {
            if (!isInnerObject) {
                if (i < chars.length && chars[i] == ':') {
                    name = str.substring(nameIndex, i);
                    valueIndex = i + 1;
                }
                if (i == chars.length || chars[i] == ';') {
                    value = str.substring(valueIndex, i);
                    nameIndex = i + 1;
                    valuesMap.put(name, value);
                }
            }
            if (i < chars.length) {
                if (chars[i] == '[') {
                    isInnerObject = true;
                } else if (chars[i] == ']') {
                    isInnerObject = false;
                }
            }
        }
        return valuesMap;
    }

    private void setValuesToObject(Class<?> usedClass, Object transport, HashMap<String, String> valuesMap)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Method[] methods = usedClass.getMethods();
        SetMethod setMethod;
        ArrayList<String> names = new ArrayList<>(valuesMap.keySet());
        for (String name : names) {
            for (Method method : methods) {
                setMethod = method.getAnnotation(SetMethod.class);
                if (setMethod != null && name.equals(setMethod.name())) {
                    Class<?> parameterType = method.getParameterTypes()[0];
                    if (!parameterType.isEnum()) {
                        switch (setMethod.typeParameter()) {
                            case "Integer" -> {
                                method.invoke(transport, Integer.parseInt(valuesMap.get(name)));
                            }
                            case "String" -> {
                                method.invoke(transport, valuesMap.get(name));
                            }
                            case "Double" -> {
                                method.invoke(transport, Double.parseDouble(valuesMap.get(name)));
                            }
                            case "Boolean" -> {
                                method.invoke(transport, Boolean.parseBoolean(valuesMap.get(name)));
                            }
                            default -> {
                                Class[] types = null;
                                Object object = parameterType.getConstructor(types).newInstance();
                                String objectStr = valuesMap.get(name);
                                HashMap<String, String> objectMap = textToMap(objectStr.substring(1, objectStr.length() - 1));
                                setValuesToObject(parameterType, object, objectMap);
                                method.invoke(transport, object);
                            }
                        }
                    } else {
                        Field[] fields = parameterType.getFields();
                        EnumInformation enumInformation;
                        for (Field field : fields) {
                            enumInformation = field.getAnnotation(EnumInformation.class);
                            if (enumInformation.name().equals(valuesMap.get(name))) {
                                method.invoke(transport, field.get(parameterType));
                            }
                        }
                    }
                }
            }
        }
    }


    private String valuesToString(Class<?> clazz, Object object) throws InvocationTargetException, IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Класс:").append(clazz.getSimpleName()).append(';');
        Method[] methods = clazz.getMethods();
        GetMethod getMethod;
        for (Method method : methods) {
            getMethod = method.getAnnotation(GetMethod.class);
            if (getMethod != null) {
                stringBuilder.append(getMethod.name()).append(":");
                Class<?> returnedType = method.getReturnType();
                if (returnedType.isEnum()) {
                    Field[] fields = returnedType.getFields();
                    EnumInformation enumInformation;
                    for (Field field : fields) {
                        enumInformation = field.getAnnotation(EnumInformation.class);
                        if (method.invoke(object).equals(field.get(returnedType))) {
                            stringBuilder.append(enumInformation.name());
                        }
                    }
                } else {
                    switch (getMethod.returnType()) {
                        case "String", "Integer", "Double", "Boolean" -> {
                            stringBuilder.append(method.invoke(object));
                        }
                        default -> {
                            Object innerObject = method.invoke(object);
                            stringBuilder.append("[");
                            stringBuilder.append(valuesToString(returnedType, innerObject));
                            stringBuilder.append("]");
                        }
                    }
                }
                stringBuilder.append(';');
            }
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        return stringBuilder.toString();
    }
}
