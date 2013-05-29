package com.lavacraftserver.HarryPotterSpells.Jobs;

import org.bukkit.plugin.PluginManager;

/**
 * A job that is called when the plugin is enabled
 */
public interface EnableJob {
	
	void onEnable(PluginManager pm);

}
