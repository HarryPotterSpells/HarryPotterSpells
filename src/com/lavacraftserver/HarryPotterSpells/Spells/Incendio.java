package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Incendio",
		description="Shoots a random mob from your wand",
		range=50,
		goThroughWalls=false
)
public class Incendio extends Spell{

	public void cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			int fireTicks = HPS.Plugin.getConfig().getInt("spells.incendio.duration");
			le.setFireTicks(fireTicks);
		} else {
			Block b = p.getTargetBlock(null, this.getRange());
			Material m = b.getType();
			if(m != Material.AIR) {
				Block above = new Location(b.getWorld(), b.getX(), b.getY() + 1, b.getZ()).getBlock();
				if(Targeter.getTransparentBlocks().contains(above.getTypeId()))
					above.setType(Material.FIRE);
			}
		}
	}
}

