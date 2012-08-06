package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Reducto {
	
	public static void cast(Player p) {
		Block b = p.getTargetBlock(null, 50);
		Material m = b.getType();
		if(m != Material.AIR) {
			b.getWorld().createExplosion(b.getLocation(), 4, false);
		}
	}

}
