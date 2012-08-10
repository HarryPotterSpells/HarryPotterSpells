package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

public class Incendio extends Spell{

	public Incendio(HarryPotterSpells instance) {
		super(instance);
	}

	public void cast(Player p) {
		if(Targeter.getTarget(p, 50) instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) Targeter.getTarget(p, 50);
			int fireTicks = plugin.getConfig().getInt("incendio.duration");
			le.setFireTicks(fireTicks);
		} else {
			Block b = p.getTargetBlock(null, 50);
			Material m = b.getType();
			if(m != Material.AIR) {
				//Get the location, add 1 to Y, set fire
			}
		}
	}
}

