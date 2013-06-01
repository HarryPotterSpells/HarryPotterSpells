package com.lavacraftserver.HarryPotterSpells.Extensions;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;

import com.lavacraftserver.HarryPotterSpells.HPS;

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
	
	/*
	 * START PROTECTED FUNCTIONS
	 */
	
	protected void setDescription(ExtensionDescription description) {
		this.description = description;
	}
	
	protected void initiate(File dataFolder) {
		this.dataFolder = dataFolder;
		this.config = new File(dataFolder, "config.yml");
		
		try {
			if(!dataFolder.exists())
				dataFolder.mkdir();
			if(!config.exists())
				config.createNewFile();
		} catch(IOException e) {
			HPS.PM.log(Level.WARNING, "An error occurred whilst creating files for the " + description.getName() + " extenstion.");
			if(HPS.Plugin.getConfig().getBoolean("debug-mode", false))
				e.printStackTrace();
		}
	}

}
