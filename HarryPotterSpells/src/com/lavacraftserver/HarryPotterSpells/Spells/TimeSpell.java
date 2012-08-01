package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class TimeSpell {
	//Dawn == 0L
	//Morning == 2500L
	//Noon == 6000L
	//Evening == 11000L
	//Night == 15000L

	public static void cast(Player p, PlayerInteractEvent e) {
		if(e.getClickedBlock().getType() == Material.GLOWSTONE) {
			p.getWorld().setTime(0L);
		} else if(e.getClickedBlock().getType() == Material.OBSIDIAN) {
			p.getWorld().setTime(15000L);
		} else {
			long time = p.getWorld().getTime();
			if(time < 12000) {
				time = time + 12000L;
			} else {
				time = time - 12000L;
			}
			p.getWorld().setTime(time);
		}
	}
	
}
