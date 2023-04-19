package com.example.plugins;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Set;

public class ManagePlugins {
    public static Set<Class<? extends EncryptionPlugin>> getPluginClasses(String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(EncryptionPlugin.class);
    }

    public static ArrayList<EncryptionPlugin> createPlugins(ArrayList<Class<? extends EncryptionPlugin>> plugins) {
        ArrayList<EncryptionPlugin> pluginsList = new ArrayList<>();
        EncryptionPlugin plugin = null;
        for (Class<? extends EncryptionPlugin> clazz : plugins) {
            try {
                plugin = clazz.newInstance();
                pluginsList.add(plugin);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return pluginsList;
    }

    public static EncryptionPlugin createPlugin(ArrayList<Class<? extends EncryptionPlugin>> plugins, final String name) {
        EncryptionPlugin plugin = null;
        for (Class<? extends EncryptionPlugin> clazz : plugins) {
            try {
                plugin = clazz.newInstance();
                if (name.equals(plugin.getName())) {
                    return plugin;
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return plugin;
    }
}
