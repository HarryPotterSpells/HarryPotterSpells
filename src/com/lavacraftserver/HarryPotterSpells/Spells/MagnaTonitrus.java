package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell(
		name = "MagnaTonitrus", 
		description = "Shoots a bolt of lightning", 
		range = 50, 
		goThroughWalls = false
)

public class MagnaTonitrus extends Spell {

	public void cast(Player p) {
		Block b = p.getTargetBlock(null, this.getRange());
		if (b.getType() != Material.AIR) {
			b.getWorld().strikeLightning(b.getLocation());
			Block above = new Location(b.getWorld(), b.getX(), b.getY() + 1, b.getZ()).getBlock();
			if (above.getType() == Material.FIRE) {
				above.setType(Material.AIR);
			}
		}
	}
}
