package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class Confringo {
	
	public static void cast(Player p, PlayerInteractEvent e) {
		p.launchProjectile(Fireball.class);
	}
}
