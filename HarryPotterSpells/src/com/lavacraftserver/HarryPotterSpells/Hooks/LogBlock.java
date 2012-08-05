package com.lavacraftserver.HarryPotterSpells.Hooks;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.lavacraftserver.HarryPotterSpells.PM;

import de.diddiz.LogBlock.Consumer;

public class LogBlock {
	public static Consumer LogBlockConsumer;
	public static de.diddiz.LogBlock.LogBlock LogBlockPlugin;
	
	public static void setupLogBlock() {
		if(PM.hps.getConfig().getBoolean("LogBlockEnabled")) {
			Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("LogBlock");
			if(plugin != null) {
				LogBlockConsumer = ((de.diddiz.LogBlock.LogBlock)plugin).getConsumer();
				LogBlockPlugin = (de.diddiz.LogBlock.LogBlock)plugin;
			} else {
				PM.log("Could not hook into LogBlock. LogBlock features have been disabled.", Level.WARNING);
				PM.hps.getConfig().set("LogBlockEnabled", false);
			}
		}
	}

	public static void logSpell(Player p, String spell) {
		if(LogBlockConsumer != null) {
			LogBlockConsumer.queueChat(p.getName(), "SPELL CAST: " + spell);
		}
		
	}

}
