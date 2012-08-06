package com.lavacraftserver.HarryPotterSpells;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerSpellConfig extends JavaPlugin {
	private static FileConfiguration PSC = null;
	private static File PSCFile = null;
	
	public static void reloadPSC() {
	    if (PSCFile == null) {
	    	PSCFile = new File(PM.hps.getDataFolder(), "PlayerSpellConfig.yml");
	    }
	    PSC = YamlConfiguration.loadConfiguration(PSCFile);
	    InputStream defConfigStream = PM.hps.getResource("PlayerSpellConfig.yml");
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        PSC.setDefaults(defConfig);
	    }
	    savePSC();
	}
	
	public static FileConfiguration getPSC() {
	    if (PSC == null) {
	        reloadPSC();
	    }
	    return PSC;
	}
	
	public static void savePSC() {
	    if (PSC == null || PSCFile == null) {return;}
	    try {
	        getPSC().save(PSCFile);
	    } catch (IOException ex) {
	        PM.log("Could not save config to " + PSCFile + ". Error: " + ex.getMessage(), Level.SEVERE);
	    }
	}

}
