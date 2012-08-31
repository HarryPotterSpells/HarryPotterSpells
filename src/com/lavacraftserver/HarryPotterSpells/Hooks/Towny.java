package com.lavacraftserver.HarryPotterSpells.Hooks;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

// JavaDocs http://palmergames.com/javadoc/
public class Towny {
	HarryPotterSpells plugin;

	public Towny(HarryPotterSpells instance) {
		plugin = instance;
	}
	
	public com.palmergames.bukkit.towny.Towny Towny;
	
	public void setupTowny() {
		if(plugin.getConfig().getBoolean("TownyEnabled") != false) {
			Plugin townyplugin = Bukkit.getServer().getPluginManager().getPlugin("Towny");
	 
			// Towny may not be loaded
			if (townyplugin == null || !(townyplugin instanceof Towny)) {
				plugin.PM.log("Could not hook into Towny. Towny features have been disabled.", Level.WARNING);
				plugin.getConfig().set("TownyEnabled", false);
				return;
			}
			
			Towny = (com.palmergames.bukkit.towny.Towny)townyplugin;
			
			return;
		}
	}
	
	public boolean isEnabled() {
		if(Towny != null) {
			return true;
		} else {
			return false;
		}
	}

}
