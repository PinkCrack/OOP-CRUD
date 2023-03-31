package com.example.factories;

import com.example.annotation.EnumInformation;
import com.example.annotation.GetMethod;
import com.example.annotation.SetMethod;
import com.example.model.Transport;
import com.example.annotation.FieldInformation;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public abstract class TransportFactory {

    public abstract Transport createTransport();

    public HBox render() {
        HBox hBox = new HBox();
        VBox labels = new VBox();
        labels.setSpacing(8);
        VBox controls = new VBox();
        controls.setSpacing(10);

        Transport transport = createTransport();
        Class<?> usedClass = transport.getClass();
        ArrayList<Control> listOfControls = new ArrayList<>();
        ArrayList<Label> listOfLabels = new ArrayList<>();

        while (!usedClass.getSimpleName().equalsIgnoreCase("Object")) {
            listOfControls.addAll(getControls(usedClass));
            listOfLabels.addAll(getLabels(usedClass, ""));
            usedClass = usedClass.getSuperclass();
        }

        controls.getChildren().addAll(listOfControls);
        labels.getChildren().addAll(listOfLabels);

        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(labels);
        hBox.getChildren().add(controls);

        return hBox;
    }

    public void setValuesToControls(HBox controlsHBox, Transport transport) {
        VBox labelsVBox = (VBox) controlsHBox.getChildren().get(0);
        VBox controlsBox = (VBox) controlsHBox.getChildren().get(1);
        ObservableList<Node> listOfLabels = labelsVBox.getChildren();
        ObservableList<Node> listOfControls = controlsBox.getChildren();

        Class<?> usedClass = transport.getClass();
        while (!usedClass.getSimpleName().equalsIgnoreCase("Object")) {
            getValuesFromObject(usedClass, transport, listOfLabels, listOfControls);
            usedClass = usedClass.getSuperclass();
        }
    }

    public void getValuesFromControls(HBox controlsHBox, Transport transport) {
        VBox labelsVBox = (VBox) controlsHBox.getChildren().get(0);
        VBox controlsBox = (VBox) controlsHBox.getChildren().get(1);
        ObservableList<Node> listOfLabels = labelsVBox.getChildren();
        ObservableList<Node> listOfControls = controlsBox.getChildren();
        Class<?> usedClass = transport.getClass();
        while (!usedClass.getSimpleName().equalsIgnoreCase("Object")) {
            setValuesToObject(usedClass, transport, listOfLabels, listOfControls);
            usedClass = usedClass.getSuperclass();
        }
    }

    public boolean checkValuesOfControls(HBox controlsHBox) {
        boolean isCorrect = true;

        VBox labelsVBox = (VBox) controlsHBox.getChildren().get(0);
        VBox controlsBox = (VBox) controlsHBox.getChildren().get(1);
        ObservableList<Node> listOfLabels = labelsVBox.getChildren();
        ObservableList<Node> listOfControls = controlsBox.getChildren();
        Transport transport = createTransport();
        Class<?> usedClass = transport.getClass();
        while (!usedClass.getSimpleName().equalsIgnoreCase("Object")) {
            if (!checkValues(usedClass, listOfLabels, listOfControls)) {
                isCorrect = false;
            }
            usedClass = usedClass.getSuperclass();
        }
        return isCorrect;
    }

    private ArrayList<Control> getControls(Class<?> usedClass) {
        ArrayList<Control> controls = new ArrayList<>();
        Field[] fields = usedClass.getDeclaredFields();
        FieldInformation annotation;
        for (Field field : fields) {
            if (!field.getType().isEnum()) {
                annotation = field.getAnnotation(FieldInformation.class);
                switch (annotation.type()) {
                    case "Double", "Integer", "String" -> {
                        TextField textField = new TextField();
                        controls.add(textField);
                    }
                    case "Boolean" -> {
                        CheckBox checkBox = new CheckBox();
                        controls.add(checkBox);
                    }
                    default -> {
                        ArrayList<Control> list = getControls(field.getType());
                        Label emptyLabel = new Label();
                        emptyLabel.setStyle("-fx-font-size: 18px");
                        controls.add(emptyLabel);
                        controls.addAll(list);
                    }
                }
            } else {
                EnumInformation enumAnnotation;
                Field[] enumFields = field.getType().getFields();
                ComboBox comboBox = new ComboBox();
                for (Field enumField : enumFields) {
                    enumAnnotation = enumField.getAnnotation(EnumInformation.class);
                    comboBox.getItems().add(enumAnnotation.name());
                }
                controls.add(comboBox);
            }
        }

        return controls;
    }

    private ArrayList<Label> getLabels(Class<?> usedClass, final String prefix) {
        ArrayList<Label> labels = new ArrayList<>();
        Field[] fields = usedClass.getDeclaredFields();
        FieldInformation annotation;
        for (Field field : fields) {
            annotation = field.getAnnotation(FieldInformation.class);
            if (!field.getType().isEnum()) {
                switch (annotation.type()) {
                    case "Double", "Integer", "String", "Boolean" -> {
                        Label label = new Label(prefix + annotation.name());
                        label.setStyle("-fx-font-size: 18px");
                        labels.add(label);
                    }
                    default -> {
                        Label label = new Label(prefix + annotation.name());
                        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold");
                        labels.add(label);
                        ArrayList<Label> list = getLabels(field.getType(), "\t");
                        labels.addAll(list);
                    }
                }
            } else {
                Label label = new Label(prefix + annotation.name());
                label.setStyle("-fx-font-size: 18px");
                labels.add(label);
            }
        }

        return labels;
    }

    private void getValuesFromObject(Class<?> usedClass, Object transport, ObservableList<Node> listOfLabels,
                                     ObservableList<Node> listOfControls) {
        Method[] methods = usedClass.getMethods();
        GetMethod getMethod;
        String name;
        Label label;
        for (Method method : methods) {
            getMethod = method.getAnnotation(GetMethod.class);
            if (getMethod != null) {
                name = getMethod.name();
                for (int i = 0; i < listOfLabels.size(); i++) {
                    label = (Label) listOfLabels.get(i);
                    if (label.getText().trim().equals(name)) {
                        Class<?> parameterType = method.getReturnType();
                        try {
                            if (!parameterType.isEnum()) {
                                switch (getMethod.returnType()) {
                                    case "Double" -> {
                                        TextField textField = (TextField) listOfControls.get(i);
                                        textField.setText(Double.toString((Double) method.invoke(transport)));
                                    }
                                    case "Integer" -> {
                                        TextField textField = (TextField) listOfControls.get(i);
                                        textField.setText(Integer.toString((Integer) method.invoke(transport)));
                                    }
                                    case "String" -> {
                                        TextField textField = (TextField) listOfControls.get(i);
                                        textField.setText((String) method.invoke(transport));
                                        if (label.getText().trim().equals("VIN")) {
                                            textField.setDisable(true);
                                        }
                                    }
                                    case "Boolean" -> {
                                        CheckBox checkBox = (CheckBox) listOfControls.get(i);
                                        checkBox.setSelected((Boolean) method.invoke(transport));
                                    }
                                    default -> {
                                        Object object = method.invoke(transport);
                                        getValuesFromObject(parameterType, object, listOfLabels, listOfControls);
                                    }
                                }
                            } else {
                                ComboBox comboBox = (ComboBox) listOfControls.get(i);
                                Field[] fields = parameterType.getFields();

                                EnumInformation enumInformation;
                                for (Field field : fields) {
                                    enumInformation = field.getAnnotation(EnumInformation.class);
                                    if (method.invoke(transport).equals(field.get(parameterType))) {
                                        comboBox.setValue(enumInformation.name());
                                    }
                                }
                            }
                        } catch (InvocationTargetException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    private void setValuesToObject(Class<?> usedClass, Object transport, ObservableList<Node> listOfLabels,
                                   ObservableList<Node> listOfControls) {
        Method[] methods = usedClass.getMethods();
        SetMethod setMethod;
        String name;
        for (Method method : methods) {
            setMethod = method.getAnnotation(SetMethod.class);
            if (setMethod != null) {
                name = setMethod.name();
                for (int i = 0; i < listOfLabels.size(); i++) {
                    Label label = (Label) listOfLabels.get(i);
                    if (label.getText().trim().equals(name)) {
                        Class<?> parameterType = method.getParameterTypes()[0];
                        try {
                            if (!parameterType.isEnum()) {
                                switch (setMethod.typeParameter()) {
                                    case "Integer" -> {
                                        TextField textField = (TextField) listOfControls.get(i);
                                        method.invoke(transport, Integer.parseInt(textField.getText()));
                                    }
                                    case "String" -> {
                                        TextField textField = (TextField) listOfControls.get(i);
                                        method.invoke(transport, textField.getText());
                                    }
                                    case "Double" -> {
                                        TextField textField = (TextField) listOfControls.get(i);
                                        method.invoke(transport, Double.parseDouble(textField.getText()));
                                    }
                                    case "Boolean" -> {
                                        CheckBox checkBox = (CheckBox) listOfControls.get(i);
                                        method.invoke(transport, checkBox.isSelected());
                                    }
                                    default -> {
                                        Class[] types = null;
                                        Object object = parameterType.getConstructor(types).newInstance();
                                        setValuesToObject(parameterType, object, listOfLabels, listOfControls);
                                        method.invoke(transport, object);
                                    }
                                }
                            } else {
                                ComboBox comboBox = (ComboBox) listOfControls.get(i);
                                Field[] fields = parameterType.getFields();
                                EnumInformation enumInformation;
                                for (Field field : fields) {
                                    enumInformation = field.getAnnotation(EnumInformation.class);
                                    if (enumInformation.name().equals((String)comboBox.getValue())) {
                                        method.invoke(transport, field.get(parameterType));
                                    }
                                }
                            }
                        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | InstantiationException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    private boolean checkValues(Class<?> usedClass, ObservableList<Node> listOfLabels,
                             ObservableList<Node> listOfControls) {
        Field[] fields = usedClass.getDeclaredFields();
        FieldInformation annotation;
        String name;
        boolean isCorrect = true;
        for (Field field : fields) {
            annotation = field.getAnnotation(FieldInformation.class);
            if (annotation != null) {
                name = annotation.name();
                for (int i = 0; i < listOfLabels.size(); i++) {
                    Label label = (Label) listOfLabels.get(i);
                    if (label.getText().trim().equals(name)) {
                        switch (annotation.type()) {
                            case "Integer" -> {
                                TextField textField = (TextField) listOfControls.get(i);
                                if (textField.getText().matches("[1-9]\\d*")) {
                                    textField.setStyle("");
                                } else {
                                    textField.setStyle("-fx-border-color: red");
                                    isCorrect = false;
                                }
                            }
                            case "Double" -> {
                                TextField textField = (TextField) listOfControls.get(i);
                                if (textField.getText().matches("[1-9]\\d*\\.?\\d+")) {
                                    textField.setStyle("");
                                } else {
                                    textField.setStyle("-fx-border-color: red");
                                    isCorrect = false;
                                }
                            }
                            default -> {
                                if (!checkValues(field.getType(), listOfLabels, listOfControls)) {
                                    isCorrect = false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return isCorrect;
    }
}
