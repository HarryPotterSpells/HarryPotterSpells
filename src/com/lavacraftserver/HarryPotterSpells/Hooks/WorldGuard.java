package com.lavacraftserver.HarryPotterSpells.Hooks;

public class WorldGuard {
	/* TODO move to addon
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
	
	public boolean isEnabled() {
		if(WorldGuard != null) {
			return true;
		} else {
			return false;
		}
	}
	
	*/

}
