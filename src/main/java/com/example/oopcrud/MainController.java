package com.example.oopcrud;

import com.example.factoryMethod.model.*;
import com.example.factoryMethod.serialization.*;
import com.example.model.*;
import com.example.plugins.EncryptionPlugin;
import com.example.plugins.ManagePlugins;
import com.example.serialization.Serializer;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.net.URL;
import java.security.InvalidKeyException;
import java.util.*;

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
    private AnchorPane encryptionPane = new AnchorPane();
    private ComboBox<String> encryptionComboBox = new ComboBox<>();
    private Button encryptionButton = new Button();
    private Button cancelEncryptionButton = new Button("Отмена");

    private VBox content = new VBox();
    private HBox controlsHBox = new HBox();
    private ComboBox<String> comboBox = new ComboBox<>();
    private Label infoLabel = new Label();

    private Button actionButton = new Button();
    private Button backButton = new Button("Назад");

    private final double PANE_WIDTH = 500;
    private final double PANE_HEIGHT = 375;

    private final double BUTTON_WIDTH = 110;

    private final String PLUGINS_PACKAGE = "com.example.plugins";

    private final String AES = "AES";
    private final String BLOWFISH = "Blowfish";
    private final ArrayList<String> ciphersList = new ArrayList<>();
    private final ArrayList<String> extensionsList = new ArrayList<>();

    private final String BIKE = "Bike";
    private final String BUS = "Bus";
    private final String ELECTRIC_CAR = "ElectricCar";
    private final String GASOLINE_CAR = "GasolineCar";
    private final Map<String, TransportFactory> transportFactoryMap = Map.ofEntries(
            Map.entry("Bike", new BikeFactory()),
            Map.entry("Bus", new BusFactory()),
            Map.entry("ElectricCar", new ElectricCarFactory()),
            Map.entry("GasolineCar", new GasolineCarFactory()));

    private final String JSON_EXTENSION = ".json";
    private final String BINARY_EXTENSION = ".bin";
    private final String TEXT_EXTENSION = ".txt";
    private final String AES_EXTENSION = ".aes";
    private final String BLOWFISH_EXTENSION = ".blf";
    private final HashMap<String, SerializerFactory> serializerFactoryMap = new HashMap<>();

    private ArrayList<Transport> listOfTransport = new ArrayList<>();
    private final ObservableList<DataTransport> tableList = FXCollections.observableArrayList();
    private File usedFile;

    private ArrayList<EncryptionPlugin> plugins;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        plugins = ManagePlugins.createPlugins(loadPlugins());
        initializeTable();
        initializeSerializeFactoryMap();
        initializeControls();
        initializeScrollPane();
        initializeEncryptionPane();
        initializeExtensions();
        numberTableColumn.setCellValueFactory(new PropertyValueFactory<DataTransport, Integer>("number"));
        typeTabelColumn.setCellValueFactory(new PropertyValueFactory<DataTransport, String>("type"));
        vinTableColumn.setCellValueFactory(new PropertyValueFactory<DataTransport, String>("vin"));
        transportTableView.setItems(tableList);
        mainPane.getChildren().add(encryptionPane);
        comboBox.getItems().addAll(BIKE, BUS, ELECTRIC_CAR, GASOLINE_CAR);
    }

    @FXML
    private void onAddButtonClick() {
        actionButton.setText("Создать");
        infoLabel.setText("Создание объекта");
        comboBox.setDisable(false);
        controlsScrollPane.setVisible(true);
        menuBar.setVisible(false);
        actionButton.setVisible(false);
    }

    @FXML
    private void onEditButtonClick() {
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
    private void onDeleteButtonClick() {
        if (transportTableView.getSelectionModel().getSelectedItem() != null) {
            listOfTransport.remove(tableList.indexOf(transportTableView.getSelectionModel().getSelectedItem()));
            tableList.remove(transportTableView.getSelectionModel().getSelectedItem());
            for (int i = 0; i < tableList.size(); i++) {
                tableList.get(i).setNumber(i + 1);
            }
        }
    }

    @FXML
    private void onOpenMenuItemClick() {
        CustomFileChooser customFileChooser = new CustomFileChooser(plugins);
        FileChooser fileChooser = customFileChooser.createOpenFileChooser();
        File openFile = fileChooser.showOpenDialog(content.getScene().getWindow());
        if (openFile != null) {
            String[] extensions = openFile.getName().split("\\.");
            String extension = "." + extensions[extensions.length - 1];
            byte[] bytes = new byte[(int) openFile.length()];
            if (customFileChooser.isContainsExtensionsPlugin(extension)) {
                EncryptionPlugin encryptionPlugin = loadPlugin(ciphersList.get(extensionsList.indexOf(extension)));
                bytes = decryptFile(encryptionPlugin, openFile);
                extension = "." + extensions[extensions.length - 2];
            } else {
                try (FileInputStream fileInputStream = new FileInputStream(openFile)) {
                    fileInputStream.read(bytes);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (customFileChooser.isContainsExtensionsSerializer(extension)) {
                deserializeFile(extension, bytes);
            } else {
                showError("Ошибка формата!", "Файл не является (.txt, .bin или .json)");
            }
        }
    }

    @FXML
    private void onSaveMenuItemClick() {
        if (!listOfTransport.isEmpty()) {
            CustomFileChooser customFileChooser = new CustomFileChooser(plugins);
            FileChooser fileChooser = customFileChooser.createSaveFileChooser();
            File saveFile = fileChooser.showSaveDialog(content.getScene().getWindow());
            if (saveFile != null) {
                String[] str = saveFile.getName().split("\\.");
                if (customFileChooser.isContainsExtensionsSerializer(".".concat(str[str.length - 1]))) {
                    usedFile = saveFile;
                    showEncryptionPane();
                }
            }
        } else {
            showError("Ошибка!", "Список объектов пуст!");
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

    private void showError(final String headerText ,final String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(text);
        alert.show();
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

    private void encryptionComboBoxOnAction(ActionEvent event) {
        if (!Objects.equals(encryptionComboBox.getValue(), "")) {
            encryptionButton.setVisible(true);
        }
    }

    private void encryptionButtonOnAction(ActionEvent event) {
        EncryptionPlugin encryptionPlugin = loadPlugin(encryptionComboBox.getValue());
        String[] substrings = usedFile.getName().split("\\.");
        String extension = "." + substrings[substrings.length - 1];
        File encryptFile = new File(usedFile.getAbsolutePath().concat(extensionsList.
                get(ciphersList.indexOf(encryptionComboBox.getValue()))));

        serializeFile(encryptFile, extension);
        if (encryptionButton.getText().equals("Зашифровать")) {
            encryptFile(encryptionPlugin, encryptFile);
        }
        encryptionComboBox.setValue("");
        encryptionPane.setVisible(false);
        menuBar.setVisible(true);
        encryptionButton.setVisible(false);
    }

    private ArrayList<Class<? extends EncryptionPlugin>> loadPlugins() {
        return new ArrayList<>(ManagePlugins.getPluginClasses(PLUGINS_PACKAGE));
    }

    private EncryptionPlugin loadPlugin(String type) {
        ArrayList<Class<? extends EncryptionPlugin>> plugins =
                new ArrayList<>(ManagePlugins.getPluginClasses(PLUGINS_PACKAGE));
        return ManagePlugins.createPlugin(plugins, type);
    }

    private void encryptFile(EncryptionPlugin encryptionPlugin, File file) {
        byte[] bytes = new byte[(int) file.length()];
        byte[] cipherBytes;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(bytes);
            cipherBytes = encryptionPlugin.encrypt(bytes);
        } catch (IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(cipherBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] decryptFile(EncryptionPlugin encryptionPlugin, File file) {
        byte[] bytes = new byte[(int) file.length()];
        byte[] cipherBytes;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(bytes);
            cipherBytes = encryptionPlugin.decrypt(bytes);
        } catch (IOException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return cipherBytes;
    }

    private void serializeFile(File file, String extension) {
        SerializerFactory serializerFactory = serializerFactoryMap.get(extension);
        Serializer serializer = serializerFactory.createSerializer();
        serializer.serialize(file, listOfTransport);
    }

    private void deserializeFile(final String extension, final byte[] bytes) {
        SerializerFactory serializerFactory = serializerFactoryMap.get(extension);
        Serializer serializer = serializerFactory.createSerializer();

        boolean isCorrect = true;
        try {
            listOfTransport = serializer.deserialize(bytes);
        } catch (IOException | ClassNotFoundException e) {
            showError("Ошибка!", "Файл поврежден или не содержит список объектов");
            isCorrect = false;
        }
        if (isCorrect && listOfTransport != null) {
            tableList.clear();
            for (Transport transport : listOfTransport) {
                tableList.add(new DataTransport(tableList.size() + 1,
                        transport.getClass().getSimpleName(), transport.getVin()));
            }
        }
    }

    private void cancelEncryptButtonOnAction(ActionEvent event) {
        String[] substrings = usedFile.getName().split("\\.");
        String extension = "." + substrings[substrings.length - 1];
        serializeFile(usedFile, extension);
        encryptionComboBox.setValue("");
        encryptionPane.setVisible(false);
        menuBar.setVisible(true);
        encryptionButton.setVisible(false);
    }

    private void showEncryptionPane() {
        menuBar.setVisible(false);
        encryptionPane.setVisible(true);
    }

    private void initializeExtensions() {
        ciphersList.add(BLOWFISH);
        ciphersList.add(AES);

        extensionsList.add(BLOWFISH_EXTENSION);
        extensionsList.add(AES_EXTENSION);
    }

    private void initializeEncryptionPane() {
        encryptionPane.setStyle("-fx-background-color: #fafafa");
        encryptionPane.toFront();
        encryptionPane.setLayoutX(0);
        encryptionPane.setLayoutY(0);
        encryptionPane.setPrefHeight(PANE_HEIGHT);
        encryptionPane.setPrefWidth(PANE_WIDTH);
        encryptionPane.setVisible(false);
        encryptionComboBox.getItems().addAll(AES, BLOWFISH);
        encryptionButton.setPrefWidth(BUTTON_WIDTH);
        encryptionButton.setVisible(false);
        cancelEncryptionButton.setPrefWidth(BUTTON_WIDTH);
        encryptionButton.setOnAction(this::encryptionButtonOnAction);
        cancelEncryptionButton.setOnAction(this::cancelEncryptButtonOnAction);
        encryptionComboBox.setOnAction(this::encryptionComboBoxOnAction);
        encryptionButton.setText("Зашифровать");
        HBox hBox = new HBox();
        hBox.getChildren().addAll(encryptionButton, cancelEncryptionButton);
        hBox.setSpacing(100);
        hBox.setAlignment(Pos.CENTER);
        Label label = new Label("Выбор плагина");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 22px");
        VBox vBox = new VBox();
        vBox.getChildren().addAll(label, encryptionComboBox, hBox);
        vBox.setSpacing(50);
        vBox.setPrefWidth(PANE_WIDTH);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-padding: 50px 0 0 0");
        encryptionPane.getChildren().add(vBox);

    }

    private void initializeTable() {
        Transport transport = new Bike(TransportColor.BLACK, 2, 1, "GJJ31BGDK32", 33, BikeType.MOUNTAIN);
        listOfTransport.add(transport);
        tableList.add(new DataTransport(tableList.size() + 1, transport.getClass().getSimpleName(), transport.getVin()));
        transport = new ElectricCar(TransportColor.GREEN, 4, 4, "fdfg\"sdf\"dfsdf", CarBody.HATCHBACK, new Battery(100, 2000));
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

    private void initializeScrollPane() {
        controlsScrollPane.setLayoutX(0);
        controlsScrollPane.setLayoutY(0);
        controlsScrollPane.setContent(content);
        controlsScrollPane.setPrefHeight(PANE_HEIGHT);
        controlsScrollPane.setPrefWidth(PANE_WIDTH);
    }

    public Map<String, TransportFactory> getTransportFactoryMap() {
        return transportFactoryMap;
    }
}
