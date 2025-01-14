package com.hpspells.core.configuration;

import com.hpspells.core.HarryPotterSpells;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Manages all custom configuration files within the plugin
 */
public class ConfigurationManager {
	
	private static String[] configHeader = {
		"############################ #",
		"## General Configuration  ## #",
		"## http://tiny.cc/hpsconf ## #",
		"############################ #",
		"\n",
		"Only enable if you want to enable debugging mode"
	};
	
    private HarryPotterSpells HPS;
    private Map<ConfigurationType, CustomConfiguration> configurationMap = new HashMap<ConfigurationType, CustomConfiguration>();

    /**
     * Constructs a new {@link ConfigurationManager}
     *
     * @param instance an instance of {@link HarryPotterSpells}
     */
    public ConfigurationManager(HarryPotterSpells instance) {
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
     * Loads the default config
     */
    @SuppressWarnings("deprecation")
	public void loadConfig() {
        File file = new File(HPS.getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        FileConfiguration resource = HPS.getConfig();
        if (!file.exists()) {
        	HPS.saveDefaultConfig();
        } else {
        	if (configHeader != null) {
            	String header = "";
                for (int i = 0; i < configHeader.length; i++) {
                	header = header + configHeader[i];
                	if (i+2 < configHeader.length) {
                		header = header + "\n";
                	}
                }
                config.options().header(header);
        	}
        	config.setDefaults(resource.getDefaults());
        	if (HPS.getConfig().getBoolean("debug-mode")) HPS.getLogger().info(config.options().header());
        	config.options().copyDefaults(true);
        	config.options().copyHeader(true);
        	try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
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
