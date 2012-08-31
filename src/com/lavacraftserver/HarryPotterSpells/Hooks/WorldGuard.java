package com.lavacraftserver.HarryPotterSpells.Hooks;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WorldGuard {
	// API can be found here: http://wiki.sk89q.com/wiki/WorldGuard/Regions/API
	HarryPotterSpells plugin;
	
	public WorldGuard(HarryPotterSpells instance){
		plugin=instance;
	}
	
	public WorldGuardPlugin WorldGuard;
	
	public void setupWorldGuard() {
		if(plugin.getConfig().getBoolean("WorldGuardEnabled") != true) {
			return;
		} else {
			Plugin wgplugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
	 
			// WorldGuard may not be loaded
			if (wgplugin == null || !(wgplugin instanceof WorldGuardPlugin)) {
				plugin.PM.log("Could not hook into WorldGuard. WorldGuard features have been disabled.", Level.WARNING);
				plugin.getConfig().set("WorldGuardEnabled", false);
				return;
			}
			
			WorldGuard = (WorldGuardPlugin)wgplugin;
			
			return;
		}
	}

}
