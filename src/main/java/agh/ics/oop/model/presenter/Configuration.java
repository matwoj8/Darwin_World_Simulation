package agh.ics.oop.model.presenter;

import java.io.*;
import java.util.Properties;

public class Configuration {
    private final Properties properties;
    private final String configFilePath;

    public Configuration(String configFilePath) {
        this.configFilePath = configFilePath;
        this.properties = new Properties();
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void load() throws IOException {
        try (InputStream input = new FileInputStream(configFilePath)) {
            properties.load(input);
        }
    }

    public void save() throws IOException {
        try (OutputStream output = new FileOutputStream(configFilePath)) {
            properties.store(output, null);
        }
    }
}