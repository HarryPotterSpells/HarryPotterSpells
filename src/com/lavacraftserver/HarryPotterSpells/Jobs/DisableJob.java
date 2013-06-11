package com.lavacraftserver.HarryPotterSpells.Jobs;

import org.bukkit.plugin.PluginManager;

/**
 * A job that is called when the plugin is disabled
 */
public interface DisableJob {

	void onDisable(PluginManager pm);

}
