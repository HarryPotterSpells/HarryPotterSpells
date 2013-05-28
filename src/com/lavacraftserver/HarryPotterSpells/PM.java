package com.lavacraftserver.HarryPotterSpells;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PM extends JavaPlugin {
	private Logger log = Bukkit.getLogger();
	
	public void log(String message, Level level) {
		log.log(level, "[HarryPotterSpells] " + message);
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
		if (HarryPotterSpells.Plugin.getConfig().getBoolean("notify-on-spell-use") == true) {
			player.sendMessage(ChatColor.GOLD + "You have cast " + ChatColor.AQUA + spell + "!");
		}
	}
	
	public void shout(Player player, String spell) {
		if (HarryPotterSpells.Plugin.getConfig().getBoolean("shout-on-spell-use") == true) {
			player.chat(spell + "!");
		}
	}
	
	public void debug(String message) {
		if(HarryPotterSpells.Plugin.getConfig().getBoolean("DebugMode")) {
			log("[HPS - Debug] " + message, Level.INFO);
		}
	}
	
	public boolean hasPermission(String permission, Player p) {
        return p.hasPermission(permission);
	}

}
