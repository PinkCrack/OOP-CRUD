package com.example.factoryMethod.serialization;

import com.example.plugins.EncryptionPlugin;
import com.example.serialization.BinarySerializer;
import com.example.serialization.JsonSerializer;
import com.example.serialization.Serializer;
import com.example.serialization.TextSerializer;
import javafx.stage.FileChooser;

import java.util.ArrayList;
import java.util.List;

public class CustomFileChooser {
    private final ArrayList<Serializer> extensionsSerializerList = new ArrayList<>(List.of(new BinarySerializer(), new JsonSerializer(), new TextSerializer()));
    private final ArrayList<EncryptionPlugin> extensionsPluginList;

    public CustomFileChooser(ArrayList<EncryptionPlugin> plugins) {
        extensionsPluginList = plugins;
    }

    public FileChooser createOpenFileChooser() {
        FileChooser fileChooser = new FileChooser();
        ArrayList<String> extensions = new ArrayList<>();
        for (Serializer ext : extensionsSerializerList) {
            extensions.add("*".concat(ext.getExtension()));
        }
        for (EncryptionPlugin ext : extensionsPluginList) {
            extensions.add("*".concat(ext.getExtension()));
        }
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Serializer's extension", extensions));
        return fileChooser;
    }

    public FileChooser createSaveFileChooser() {
        FileChooser fileChooser = new FileChooser();
        for (Serializer extension : extensionsSerializerList) {
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Serializer's extension", "*".concat(extension.getExtension())));
        }
        return fileChooser;
    }

    public boolean isContainsExtensionsPlugin(String extension) {
        for (EncryptionPlugin ext : extensionsPluginList) {
            if (ext.getExtension().equals(extension)) {
                return true;
            }
        }
        return false;
    }

    public boolean isContainsExtensionsSerializer(String extension) {
        for (Serializer ext : extensionsSerializerList) {
            if (ext.getExtension().equals(extension)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Serializer> getExtensionsSerializerList() {
        return extensionsSerializerList;
    }

    public ArrayList<EncryptionPlugin> getExtensionsPluginList() {
        return extensionsPluginList;
    }
}
