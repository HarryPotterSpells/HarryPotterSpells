package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.PM;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;


public class AvadaKedavra {

	public static void cast(Player p) {
		if(Targeter.getTarget(p, 50) instanceof LivingEntity) {
			LivingEntity livingentity = Targeter.getTarget(p, 50);
			livingentity.setHealth(0);
			if (PM.hps.getConfig().getBoolean("shout-on-spell-use") == true) {
				p.chat("Avada Kedavra!");
			}
			if (PM.hps.getConfig().getBoolean("notify-on-spell-use") == true) {
				p.sendMessage(ChatColor.AQUA + "You have cast Avada Kedavra!");
			}
		} else {
			p.sendMessage(ChatColor.RED + "You must be targetting a player or mob!");
		}
	}
	
}
