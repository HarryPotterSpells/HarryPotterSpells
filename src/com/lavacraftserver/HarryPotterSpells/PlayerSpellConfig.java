package com.lavacraftserver.HarryPotterSpells;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerSpellConfig {
	HarryPotterSpells plugin;
	public PlayerSpellConfig(HarryPotterSpells instance){
		plugin=instance;
	}
	
	private FileConfiguration PSC = null;
	private File PSCFile = null;
	
	public void reloadPSC() {
	    if (PSCFile == null) {
	    	PSCFile = new File(plugin.getDataFolder(), "PlayerSpellConfig.yml");
	    }
	    PSC = YamlConfiguration.loadConfiguration(PSCFile);
	    InputStream defConfigStream = plugin.getResource("PlayerSpellConfig.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        PSC.setDefaults(defConfig);
	    }
	    savePSC();
	}
	
	public FileConfiguration getPSC() {
	    if (PSC == null) {
	        reloadPSC();
	    }
	    return PSC;
	}
	
	public void savePSC() {
	    if (PSC == null || PSCFile == null) {return;}
	    try {
	        getPSC().save(PSCFile);
	    } catch (IOException ex) {
	        plugin.PM.log("Could not save config to " + PSCFile + ". Error: " + ex.getMessage(), Level.SEVERE);
	    }
	}

}
