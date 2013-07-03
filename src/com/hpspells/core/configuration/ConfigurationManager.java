package com.hpspells.core.configuration;

import java.util.HashMap;
import java.util.Map;

import com.hpspells.core.HPS;

/**
 * Manages all custom configuration files within the plugin
 */
public class ConfigurationManager {
    @SuppressWarnings("unused") private HPS HPS;
    private Map<ConfigurationType, CustomConfiguration> configurationMap = new HashMap<ConfigurationType, CustomConfiguration>();

    /**
     * Constructs a new {@link ConfigurationManager}
     * @param instance an instance of {@link HPS}
     */
    public ConfigurationManager(HPS instance) {
        this.HPS = instance;
    }

    /**
     * Gets a {@link CustomConfiguration} that represents the given {@link ConfigurationType} of configuration
     * @param type what configuration file you want to retrieve
     * @return the configuration file
     */
    public CustomConfiguration getConfig(ConfigurationType type) {
        return configurationMap.get(type);
    }

    /**
     * An enum representing all the different types of {@link CustomConfiguration} that exist
     */
    public enum ConfigurationType {

    }

}
