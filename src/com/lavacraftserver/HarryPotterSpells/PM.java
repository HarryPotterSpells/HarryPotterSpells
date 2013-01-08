package com.lavacraftserver.HarryPotterSpells;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public class PM extends JavaPlugin {
	HarryPotterSpells plugin;
	public PM(HarryPotterSpells instance){
		plugin=instance;
	}
	
	public void log(String message, Level level) {
		plugin.Log.log(level, "[HarryPotterSpells] " + message);
	}
	
	public void tell(Player player, String message) {
		player.sendMessage("[" + ChatColor.GOLD + "HarryPotterSpells" + ChatColor.WHITE + "] " + ChatColor.YELLOW + message);
	}
	
	public void warn(Player player, String message) {
		player.sendMessage("[" + ChatColor.GOLD + "HarryPotterSpells" + ChatColor.WHITE + "] " + ChatColor.RED + message);
	}
	
	public void newSpell(Player player, String spell) {
		player.sendMessage(ChatColor.GOLD + "You have selected spell: " + ChatColor.AQUA + spell);
	}
	
	public void notify(Player player, String spell) {
		if (plugin.getConfig().getBoolean("notify-on-spell-use") == true) {
			player.sendMessage(ChatColor.GOLD + "You have cast " + ChatColor.AQUA + spell + "!");
		}
	}
	
	public void shout(Player player, String spell) {
		if (plugin.getConfig().getBoolean("shout-on-spell-use") == true) {
			player.chat(spell + "!");
		}
	}
	
	public void debug(String message) {
		if(plugin.getConfig().getBoolean("DebugMode")) {
			plugin.Log.log(Level.INFO, "[HPS - Debug] " + message);
		}
	}
	
	public boolean hasPermission(String permission, Player p) {
		if(plugin.Vault.perm != null) { //necessary? why not use superperms?
			return plugin.Vault.perm.has(p, permission);
		} else {
			return p.hasPermission(permission);
		}
	}
	
	public void clearStorage() {
		plugin.MiscListeners.deprimo.clear();
		plugin.MiscListeners.sonorus.clear();
		plugin.MiscListeners.spongify.clear();
		plugin.Listeners.currentSpell.clear();
	}

}
