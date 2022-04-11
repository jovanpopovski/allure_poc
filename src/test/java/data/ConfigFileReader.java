package data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigFileReader {

    private Properties properties;
    private final String propertyFilePath= "config//config.properties";

    public ConfigFileReader() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
        }
    }

    public String getWiremockServerUrl() {
        String url = properties.getProperty("wiremock.server.url");
        if(url != null) return url;
        else throw new RuntimeException("wiremock.server.url not specified in the Configuration.properties file.");
    }

    public String getWiremockServerPort() {
        String url = properties.getProperty("wiremock.server.port");
        if(url != null) return url;
        else throw new RuntimeException("wiremock.server.port not specified in the Configuration.properties file.");
    }

    public String getWiremockServerEndpoint() {
        String url = properties.getProperty("wiremock.server.endpoint");
        if(url != null) return url;
        else throw new RuntimeException("wiremock.server.endpoint not specified in the Configuration.properties file.");
    }

    public String getWiremockServerContentType() {
        String url = properties.getProperty("wiremock.server.content.type");
        if(url != null) return url;
        else throw new RuntimeException("wiremock.server.content.type not specified in the Configuration.properties file.");
    }
}