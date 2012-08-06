package com.lavacraftserver.HarryPotterSpells.Hooks;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.lavacraftserver.HarryPotterSpells.PM;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WorldGuard {
	// API can be found here: http://wiki.sk89q.com/wiki/WorldGuard/Regions/API
	
	public static WorldGuardPlugin getWorldGuard() {
		if(PM.hps.getConfig().getBoolean("WorldGuardEnabled") != true) {
			return null;
		} else {
			Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	 
			// WorldGuard may not be loaded
			if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
				PM.log("Could not hook into WorldGuard. WorldGuard features have been disabled.", Level.WARNING);
				PM.hps.getConfig().set("WorldGuardEnabled", false);
				return null;
			}
			
			return (WorldGuardPlugin) plugin;
		}
	}

}
