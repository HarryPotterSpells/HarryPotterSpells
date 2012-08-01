package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;

public class WingardiumLeviosa {
	
	public static void cast(Player p) {
		if(p.isFlying()) {
			p.setFlying(false);
		} else {
			p.setFlying(true);
		}
	}

}
