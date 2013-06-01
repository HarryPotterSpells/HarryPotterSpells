package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Reducto",
		description="Creates an explosion",
		range=50,
		goThroughWalls=false,
		cooldown=300
)
public class Reducto extends Spell {

	public boolean cast(Player p) {
		Block b = p.getTargetBlock(null, this.getRange());
		Material m = b.getType();
		if(m != Material.AIR) {
			b.getWorld().createExplosion(b.getLocation(), 4, false);
		}
		return true;
	}

}
