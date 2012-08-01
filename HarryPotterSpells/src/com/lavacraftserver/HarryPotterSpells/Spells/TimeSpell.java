package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class TimeSpell {
	//Dawn == 0L
	//Morning == 2500L
	//Noon == 6000L
	//Evening == 11000L
	//Night == 15000L

	public static void cast(Player p, PlayerInteractEvent e) {
		World w = p.getWorld();
		if(p.getEyeLocation().getBlock() != null) {
			if(p.getEyeLocation().getBlock().getType() == Material.GLOWSTONE) {
				w.setTime(0L);
				return;
			} else if(p.getEyeLocation().getBlock().getType() == Material.OBSIDIAN) {
				w.setTime(15000L);
				return;
			}
		}
		long time = w.getTime();
		if(time < 12000) {
			time = time + 12000L;
		} else {
			time = time - 12000L;
		}
		awesomeLightning(p.getLocation(), w);
		w.setTime(time);
	}

	public static void awesomeLightning(Location l, World w) {
		double x = l.getX(), y = l.getY(), z = l.getZ();
		//X Y Z
		w.strikeLightningEffect(new Location(w, x, y, z - 2));
		w.strikeLightningEffect(new Location(w, x, y, z + 2));
		w.strikeLightningEffect(new Location(w, x - 2, y, z));
		w.strikeLightningEffect(new Location(w, x + 2, y, z));
	}
	
}
