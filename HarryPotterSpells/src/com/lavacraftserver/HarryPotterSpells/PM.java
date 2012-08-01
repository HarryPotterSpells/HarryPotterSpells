package com.lavacraftserver.HarryPotterSpells;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PM extends JavaPlugin {
	public static Logger log;
	public static Plugin hps;
	
	public static void log(String message, Level level) {
		log.log(level, message);
	}
	
	public static void tell(Player player, String message) {
		player.sendMessage("[" + ChatColor.GOLD + "HarryPotterSpells" + ChatColor.WHITE + "] " + ChatColor.YELLOW + message);
	}
	
	public static void warn(Player player, String message) {
		player.sendMessage("[" + ChatColor.GOLD + "HarryPotterSpells" + ChatColor.WHITE + "] " + ChatColor.RED + message);
	}
	
	public static void newSpell(Player player, String spell) {
		player.sendMessage(ChatColor.GOLD + "You have selected spell: " + spell);
	}
	
	public static void debug(String message) {
		if(hps.getConfig().getBoolean("DebugMode")) {
			log.log(Level.INFO, "[Debug] " + message);
		}
	}
	
	public static boolean hasPermission(String permission, Player p) {
		if(Vault.perm != null) {
			return Vault.perm.has(p, permission);
		} else {
			return p.hasPermission(permission);
		}
	}

}
