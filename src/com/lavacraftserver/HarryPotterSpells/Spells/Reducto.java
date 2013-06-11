package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Reducto",
		description="descReducto",
		range=50,
		goThroughWalls=false,
		cooldown=300
)
public class Reducto extends Spell {

	public Reducto(HPS plugin) {
        super(plugin);
    }

    public boolean cast(Player p) {
		Block b = p.getTargetBlock(null, this.getRange());
		if(b.getType() != Material.AIR)
			b.getWorld().createExplosion(b.getLocation(), 4, false);
		return true;
	}

}
