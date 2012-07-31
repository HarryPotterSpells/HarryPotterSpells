package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TreeSpell {

	public static void cast(Player p) {
		Location target = p.getEyeLocation();
		Block block = p.getTargetBlock(null, 50);
		if (block == GRASS || Material.DIRT) {
			
		}
	}
}

