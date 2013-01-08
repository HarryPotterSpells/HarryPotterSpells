package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class wand extends Executor {
	public wand(HarryPotterSpells instance) {
		super(instance);
	}
	
	@Override
	public String getCommand() {
		return "wand";
	}
	
	public void runPlayer(Player sender,String[] args) {
		if(plugin.getConfig().getBoolean("WandGive.enabled")) {
			if(sender instanceof Player) {
				Player player = (Player)sender;
				player.getInventory().setItem(player.getInventory().firstEmpty(), plugin.Wand.getWand());
				plugin.PM.tell(player, "You have been given a wand!");
				if(plugin.getConfig().getBoolean("WandGive.explosionEffect")) {
					player.getWorld().createExplosion(player.getLocation(), 0, false);
				}
		}
	}
	}

}
