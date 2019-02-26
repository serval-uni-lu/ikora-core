package org.ukwikora.utils;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Configuration {

    @JsonProperty("verbose")
    private Boolean verbose;
    @JsonProperty("logger level")
    private String loggerLevel;
    @JsonIgnore
    private Map<String, Plugin> plugins = new HashMap<>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();
    @JsonIgnore
    private static Configuration instance = new Configuration();
    @JsonIgnore
    private static File configurationFolder;
    @JsonIgnore
    private final static Logger logger = LogManager.getLogger(Configuration.class);

    private Configuration(){
        verbose = true;
        loggerLevel = "INFO";
    }

    public static Configuration getInstance()
    {   return instance;
    }

    @JsonProperty("verbose")
    public Boolean isVerbose() {
        return verbose;
    }

    @JsonProperty("verbose")
    public void setVerbose(Boolean value) {
        verbose = value;
    }

    @JsonProperty("logger level")
    public String getLoggerLevel() {
        return loggerLevel;
    }

    @JsonProperty("logger level")
    public void setLoggerLevel(String value) {
        loggerLevel = value;
    }

    @JsonProperty("plugins")
    public List<Plugin> getPlugins() {
        return new ArrayList<>(plugins.values());
    }

    @JsonProperty("plugins")
    public void setPlugins(List<Plugin> value) {
        plugins = new HashMap<>();
        for(Plugin plugin: value){
            plugins.put(plugin.getName(), plugin);
        }
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
    }

    public boolean hasPlugin(String plugin){
        return plugins.containsKey(plugin);
    }

    public Plugin getPlugin(String plugin){
        return plugins.get(plugin);
    }

    public File getConfigurationFolder(){
        return configurationFolder;
    }

    public static void initialize(String config) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(config);

        instance = mapper.readValue(file, Configuration.class);

        AppDirs appDirs = AppDirsFactory.getInstance();
        String configPath = appDirs.getUserDataDir("RobotFramework", "1.0", "Serval");

        configurationFolder = new File(configPath);
        configurationFolder.mkdirs();

        logger.info("Configuration loaded from " + config);
    }
}