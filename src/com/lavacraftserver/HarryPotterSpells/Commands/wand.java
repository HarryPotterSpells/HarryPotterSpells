package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class wand extends Executor {
	
	@Override
	public String getCommand() {
		return "wand";
	}
	
	public void runPlayer(Player sender,String[] args) {
		if(HarryPotterSpells.Plugin.getConfig().getBoolean("WandGive.enabled")) {
			if(sender instanceof Player) {
				Player player = (Player)sender;
				player.getInventory().setItem(player.getInventory().firstEmpty(), HarryPotterSpells.Wand.getWand());
				HarryPotterSpells.PM.tell(player, "You have been given a wand!");
				if(HarryPotterSpells.Plugin.getConfig().getBoolean("WandGive.explosionEffect")) {
					player.getWorld().createExplosion(player.getLocation(), 0, false);
				}
		}
	}
	}

}
