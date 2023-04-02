package com.example.oopcrud;

import com.example.factory.*;
import com.example.model.*;
import com.example.serialization.Serializer;
import com.example.serialization.factory.BinarySerializerFactory;
import com.example.serialization.factory.JsonSerializerFactory;
import com.example.serialization.factory.SerializerFactory;
import com.example.serialization.factory.TextSerializerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public AnchorPane mainPane;
    @FXML
    public TableColumn<DataTransport, Integer> numberTableColumn;
    @FXML
    public TableColumn<DataTransport, String> typeTabelColumn;
    @FXML
    public TableColumn<DataTransport, String> vinTableColumn;
    @FXML
    public MenuItem helpMenuItem;
    @FXML
    public MenuItem saveMenuItem;
    @FXML
    public MenuItem openMenuItem;
    @FXML
    private ScrollPane controlsScrollPane;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private MenuBar menuBar;
    @FXML
    private TableView<DataTransport> transportTableView;

    private VBox content = new VBox();
    private HBox controlsHBox = new HBox();
    private ComboBox<String> comboBox = new ComboBox<>();
    private Label infoLabel = new Label();

    private Button actionButton = new Button();
    private Button backButton = new Button("Назад");

    private final double PANE_WIDTH = 500;
    private final double PANE_HEIGHT = 375;

    private final double BUTTON_WIDTH = 110;

    private final String BIKE = "Bike";
    private final String BUS = "Bus";
    private final String ELECTRIC_CAR = "ElectricCar";
    private final String GASOLINE_CAR = "GasolineCar";
    private final HashMap<String, TransportFactory> transportFactoryMap = new HashMap<>();

    private final String JSON_EXTENSION = ".json";
    private final String BINARY_EXTENSION = ".bin";
    private final String TEXT_EXTENSION = ".txt";
    private final HashMap<String, SerializerFactory> serializerFactoryMap = new HashMap<>();

    private ArrayList<Transport> listOfTransport = new ArrayList<>();

    private ObservableList<DataTransport> tableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
        initializeTransportFactoryMap();
        initializeSerializeFactoryMap();
        initializeControls();
        initializeScrollPane();
        numberTableColumn.setCellValueFactory(new PropertyValueFactory<DataTransport, Integer>("number"));
        typeTabelColumn.setCellValueFactory(new PropertyValueFactory<DataTransport, String>("type"));
        vinTableColumn.setCellValueFactory(new PropertyValueFactory<DataTransport, String>("vin"));
        transportTableView.setItems(tableList);
        comboBox.getItems().addAll(BIKE, BUS, ELECTRIC_CAR, GASOLINE_CAR);
    }

    @FXML
    private void onAddButtonClick(ActionEvent event) {
        actionButton.setText("Создать");
        infoLabel.setText("Создание объекта");
        comboBox.setDisable(false);
        controlsScrollPane.setVisible(true);
        menuBar.setVisible(false);
        actionButton.setVisible(false);
    }

    @FXML
    private void onEditButtonClick(ActionEvent event) {
        if (transportTableView.getSelectionModel().getSelectedItem() != null) {
            menuBar.setVisible(false);
            actionButton.setText("Редактировать");
            infoLabel.setText("Редактирование объекта");

            int index = tableList.indexOf(transportTableView.getSelectionModel().getSelectedItem());
            Transport transport = listOfTransport.get(index);
            TransportFactory transportFactory = transportFactoryMap
                    .get(transport.getClass().getSimpleName());

            HBox transportControls = transportFactory.render();
            content.getChildren().set(content.getChildren().size() - 2, transportControls);
            comboBox.setValue(listOfTransport.get(index).getClass().getSimpleName());
            comboBox.setDisable(true);

            HBox controlsHBox = (HBox) content.getChildren().get(2);
            transportFactory.setValuesToControls(controlsHBox, transport);

            controlsScrollPane.setVisible(true);
        }
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        if (transportTableView.getSelectionModel().getSelectedItem() != null) {
            listOfTransport.remove(tableList.indexOf(transportTableView.getSelectionModel().getSelectedItem()));
            tableList.remove(transportTableView.getSelectionModel().getSelectedItem());
            for (int i = 0; i < tableList.size(); i++) {
                tableList.get(i).setNumber(i + 1);
            }
        }
    }

    @FXML
    private void onOpenMenuItemClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serializer's extensions (*.txt, *.json, *.bin)", "*.txt", "*.json", "*.bin"));
        File openFile = fileChooser.showOpenDialog(content.getScene().getWindow());
        if (openFile != null) {
            if (openFile.getName().endsWith(BINARY_EXTENSION) || openFile.getName().endsWith(JSON_EXTENSION)
                    || openFile.getName().endsWith(TEXT_EXTENSION)) {

                String[] substrings = openFile.getName().split("\\.");
                SerializerFactory serializerFactory = serializerFactoryMap.get("." + substrings[substrings.length - 1]);
                Serializer serializer = serializerFactory.createSerializer();

                boolean isCorrect = true;
                try {
                    listOfTransport = serializer.deserialize(openFile);
                } catch (IOException | ClassNotFoundException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Ошибка!");
                    alert.setContentText("Файл поврежден или не содержит список объектов");
                    alert.show();
                    isCorrect = false;
                }
                if (isCorrect) {
                    tableList.clear();
                    if (listOfTransport != null) {
                        for (Transport transport : listOfTransport) {
                            tableList.add(new DataTransport(tableList.size() + 1, transport.getClass().getSimpleName(), transport.getVin()));
                        }
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Ошибка формата!");
                alert.setContentText("Файл не является (.txt, .bin или .json)");
                alert.show();
            }
        }
    }

    @FXML
    private void onSaveMenuItemClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serializer's extension (*.txt)", "*.txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serializer's extension (*.json)", "*.json"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serializer's extension (*.bin)", "*.bin"));
        File saveFile = fileChooser.showSaveDialog(content.getScene().getWindow());
        if (saveFile != null) {
            if (saveFile.getName().endsWith(BINARY_EXTENSION) || saveFile.getName().endsWith(JSON_EXTENSION)
                    || saveFile.getName().endsWith(TEXT_EXTENSION)) {

                String[] substrings = saveFile.getName().split("\\.");
                SerializerFactory serializerFactory = serializerFactoryMap.get("." + substrings[substrings.length - 1]);
                Serializer serializer = serializerFactory.createSerializer();
                serializer.serialize(saveFile, listOfTransport);
            }
        }
    }

    @FXML
    private void comboBoxOnAction(ActionEvent event) {
        if (!Objects.equals(comboBox.getValue(), "")) {
            TransportFactory transportFactory = transportFactoryMap.get(comboBox.getValue());
            HBox transportControls = transportFactory.render();
            content.getChildren().set(content.getChildren().size() - 2, transportControls);
            actionButton.setVisible(true);
        }
    }

    private void actionButtonOnAction(ActionEvent event) {
        HBox controlsHBox = (HBox) content.getChildren().get(content.getChildren().size() - 2);
        TransportFactory transportFactory = transportFactoryMap.get(comboBox.getValue());
        boolean isCorrect = transportFactory.checkValuesOfControls(controlsHBox);
        if (isCorrect) {
            Transport transport;
            if (actionButton.getText().equalsIgnoreCase("Создать")) {
                transport = transportFactory.createTransport();
                transportFactory.getValuesFromControls(controlsHBox, transport);

                listOfTransport.add(transport);
                tableList.add(new DataTransport(tableList.size() + 1,
                        transport.getClass().getSimpleName(), transport.getVin()));
            } else {
                transport = listOfTransport.get(tableList.indexOf(transportTableView.getSelectionModel().getSelectedItem()));
                transportFactory.getValuesFromControls(controlsHBox, transport);

                int index = tableList.indexOf(transportTableView.getSelectionModel().getSelectedItem());
                listOfTransport.set(index, transport);
                tableList.set(index, new DataTransport(index + 1,
                        transport.getClass().getSimpleName(), transport.getVin()));
            }
            comboBox.setValue("");
            controlsScrollPane.setVisible(false);
            menuBar.setVisible(true);
            content.getChildren().set(content.getChildren().size() - 2, this.controlsHBox);
        }
    }

    private void backButtonOnAction(ActionEvent event) {
        comboBox.setValue("");
        menuBar.setVisible(true);
        controlsScrollPane.setVisible(false);
        content.getChildren().set(content.getChildren().size() - 2, controlsHBox);
        actionButton.setVisible(true);
    }

    private void initializeTable() {
        Transport transport = new Bike(TransportColor.BLACK, 2, 1, "GJJ31BGDK32", 33, BikeType.MOUNTAIN);
        listOfTransport.add(transport);
        tableList.add(new DataTransport(tableList.size() + 1, transport.getClass().getSimpleName(), transport.getVin()));
        transport = new ElectricCar(TransportColor.GREEN, 4, 4, "B74ODSW43TKSL43", CarBody.HATCHBACK, new Battery(100, 2000));
        listOfTransport.add(transport);
        tableList.add(new DataTransport(tableList.size() + 1, transport.getClass().getSimpleName(), transport.getVin()));
        transport = new Bus(TransportColor.RED, 6, 28, "NFD34K2DK23", 32);
        listOfTransport.add(transport);
        tableList.add(new DataTransport(tableList.size() + 1, transport.getClass().getSimpleName(), transport.getVin()));
        transport = new GasolineCar(TransportColor.RED, 4, 7, "SLDF2323LFS", CarBody.CROSSOVER, GasolineType.PREMIUM98);
        listOfTransport.add(transport);
        tableList.add(new DataTransport(tableList.size() + 1, transport.getClass().getSimpleName(), transport.getVin()));
    }

    private void initializeControls() {
        HBox buttonsHBox = new HBox();
        buttonsHBox.setPrefWidth(PANE_WIDTH - 20);
        buttonsHBox.setSpacing(150);
        buttonsHBox.getChildren().addAll(actionButton, backButton);
        buttonsHBox.setAlignment(Pos.CENTER);
        buttonsHBox.setStyle("-fx-padding: 0 0 25px 0");

        actionButton.setPrefWidth(BUTTON_WIDTH);
        actionButton.setOnAction(this::actionButtonOnAction);
        backButton.setPrefWidth(BUTTON_WIDTH);
        backButton.setOnAction(this::backButtonOnAction);
        comboBox.setOnAction(this::comboBoxOnAction);
        infoLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 22px");

        content.setAlignment(Pos.CENTER);
        content.setSpacing(20);
        content.getChildren().addAll(infoLabel, comboBox, controlsHBox, buttonsHBox);
    }

    private void initializeSerializeFactoryMap() {
        serializerFactoryMap.put(BINARY_EXTENSION, new BinarySerializerFactory());
        serializerFactoryMap.put(JSON_EXTENSION, new JsonSerializerFactory());
        serializerFactoryMap.put(TEXT_EXTENSION, new TextSerializerFactory());
    }

    private void initializeTransportFactoryMap() {
        transportFactoryMap.put(BIKE, new BikeFactory());
        transportFactoryMap.put(BUS, new BusFactory());
        transportFactoryMap.put(ELECTRIC_CAR, new ElectricCarFactory());
        transportFactoryMap.put(GASOLINE_CAR, new GasolineCarFactory());
    }

    private void initializeScrollPane() {
        controlsScrollPane.setLayoutX(0);
        controlsScrollPane.setLayoutY(0);
        controlsScrollPane.setContent(content);
        controlsScrollPane.setPrefHeight(PANE_HEIGHT);
        controlsScrollPane.setPrefWidth(PANE_WIDTH);
    }
}
