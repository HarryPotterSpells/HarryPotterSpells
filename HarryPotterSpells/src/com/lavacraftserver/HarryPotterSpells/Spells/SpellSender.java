package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class SpellSender {
	
	public static void go(String spell, Player p, PlayerInteractEvent e) {
		if(spell.equalsIgnoreCase("confringo")) {Confringo.cast(p, e);}
		if(spell.equalsIgnoreCase("timespell")) {TimeSpell.cast(p);}
		if(spell.equalsIgnoreCase("treespell")) {TreeSpell.cast(p);}
	}
}
