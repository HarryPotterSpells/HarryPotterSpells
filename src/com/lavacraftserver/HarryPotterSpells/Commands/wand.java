package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class wand {
	HarryPotterSpells plugin;
	
	public wand(HarryPotterSpells instance) {
		plugin = instance;
	}
	
	public void run(CommandSender sender, String[] args, HarryPotterSpells plugin) {
		if(plugin.getConfig().getBoolean("WandGive.enabled")) {
			if(sender instanceof Player) {
				Player player = (Player)sender;
				player.getInventory().setItem(player.getInventory().firstEmpty(), plugin.Wand.getWand());
				plugin.PM.tell(player, "You have been given a Wand!");
				if(plugin.getConfig().getBoolean("WandGive.explosionEffect")) {
					player.getWorld().createExplosion(player.getLocation(), 0, false);
				}
			} else {
				plugin.PM.log("This command cannot be run from the console", Level.INFO);
			}	
		}
	}

}
