package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpellSender {
	
	public static void go(String spell, Player p, PlayerInteractEvent e) {
		if(spell == "confringo") {Confringo.cast(p, e);}
	}

}
