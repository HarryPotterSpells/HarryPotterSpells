package com.lavacraftserver.HarryPotterSpells.Extensions;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;

public abstract class Extension {
	private ExtensionDescription description;
	private File dataFolder, config;
	
	/**
	 * Function called when the extension is enabled
	 * @param pm HPS's PluginManager. Used for registering commands/permissions/events.
	 */
	public abstract void enable(PluginManager pm);
	
	/**
	 * Function called when the extension is disabled
	 * @param pm HPS's PluginManager. Used for registering commands/permissions/events.
	 */
	public abstract void disable(PluginManager pm);
	
	/**
	 * Gets the folder this extension should store files in
	 * @returns the data folder
	 */
	public File getDataFolder() {
		return dataFolder;
	}
	
	/**
	 * Gets the config for this extension
	 * @return the config
	 */
	public FileConfiguration getConfig() {
		return YamlConfiguration.loadConfiguration(config);
	}
	
	/**
	 * Gets the {@link ExtensionDescription} for this extension
	 * @return the extension description
	 */
	public ExtensionDescription getDescription() {
	    return description;
	}
	
	/*
	 * START PROTECTED FUNCTIONS
	 */
	
	protected void setDescription(ExtensionDescription description) {
		this.description = description;
	}
	
	protected void initiate(File dataFolder) throws IOException {
		this.dataFolder = dataFolder;
		this.config = new File(dataFolder, "config.yml");
		
		if(!dataFolder.exists())
			dataFolder.mkdir();
		if(!config.exists())
			config.createNewFile();
	}

}
