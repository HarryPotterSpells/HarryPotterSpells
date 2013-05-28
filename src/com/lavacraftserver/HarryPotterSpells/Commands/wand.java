package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;

public class wand extends Executor {
	
	@Override
	public String getCommand() {
		return "wand";
	}
	
	public void runPlayer(Player sender,String[] args) {
		if(HPS.Plugin.getConfig().getBoolean("WandGive.enabled")) {
			if(sender instanceof Player) {
				Player player = (Player)sender;
				player.getInventory().setItem(player.getInventory().firstEmpty(), HPS.Wand.getWand());
				HPS.PM.tell(player, "You have been given a wand!");
				if(HPS.Plugin.getConfig().getBoolean("WandGive.explosionEffect")) {
					player.getWorld().createExplosion(player.getLocation(), 0, false);
				}
		}
	}
	}

}
