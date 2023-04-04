package com.example.plugins;

import java.util.Set;
import org.reflections.Reflections;

public class ManagePlugins {
    public static Set<Class<? extends EncryptionPlugin>> getPluginClasses(String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(EncryptionPlugin.class);
    }

    public static EncryptionPlugin createPlugin(Set<Class<? extends EncryptionPlugin>> plugins, final String name) {
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
