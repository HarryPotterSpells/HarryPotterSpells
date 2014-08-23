package com.hpspells.core.configuration;

import com.hpspells.core.HPS;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages all custom configuration files within the plugin
 */
public class ConfigurationManager {
	
    private HPS HPS;
    private Map<ConfigurationType, CustomConfiguration> configurationMap = new HashMap<ConfigurationType, CustomConfiguration>();

    /**
     * Constructs a new {@link ConfigurationManager}
     *
     * @param instance an instance of {@link HPS}
     */
    public ConfigurationManager(HPS instance) {
        this.HPS = instance;
        configurationMap.put(ConfigurationType.COOLDOWN, new CooldownConfig(HPS, new File(HPS.getDataFolder(), "cooldown.yml"), HPS.getResource("cooldown.yml")));
        configurationMap.put(ConfigurationType.PLAYER_SPELL, new PlayerSpellConfig(HPS, new File(HPS.getDataFolder(), "PlayerSpellConfig.yml"), HPS.getResource("PlayerSpellConfig.yml")));
        configurationMap.put(ConfigurationType.SPELL, new SpellConfig(HPS, new File(HPS.getDataFolder(), "spells.yml"), HPS.getResource("spells.yml")));
    }

    /**
     * Gets a {@link CustomConfiguration} that represents the given {@link Type} of configuration
     *
     * @param type what configuration file you want to retrieve
     * @return the configuration file
     */
    public CustomConfiguration getConfig(ConfigurationType type) {
        return configurationMap.get(type);
    }
    
    /**
     * Reloads all configs
     */
    public void reloadConfigAll() {
    	configurationMap.put(ConfigurationType.COOLDOWN, new CooldownConfig(HPS, new File(HPS.getDataFolder(), "cooldown.yml"), HPS.getResource("cooldown.yml")));
    	configurationMap.put(ConfigurationType.PLAYER_SPELL, new PlayerSpellConfig(HPS, new File(HPS.getDataFolder(), "PlayerSpellConfig.yml"), HPS.getResource("PlayerSpellConfig.yml")));
    	configurationMap.put(ConfigurationType.SPELL, new SpellConfig(HPS, new File(HPS.getDataFolder(), "spells.yml"), HPS.getResource("spells.yml")));
    }

    /**
     * An enum representing all the different types of {@link CustomConfiguration} that exist
     */
    public enum ConfigurationType {
        /**
         * Stores a list of the spells that every player knows
         */
        PLAYER_SPELL,
        /**
         * Stores a list of spell configurations
         */
        SPELL,
        /**
         * Stores a list of cooldowns for spells
         */
        COOLDOWN;
    }

}
