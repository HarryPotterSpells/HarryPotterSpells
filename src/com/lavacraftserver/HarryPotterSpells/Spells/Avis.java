package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name = "Avis",
		description = "Shoots a flock of chickens from your wand",
		range = 0,
		goThroughWalls = false,
		cooldown=300
)
public class Avis extends Spell {

	public Avis(HPS plugin) {
        super(plugin);
    }

    public boolean cast(Player p) {
		int chickenAmount = (Integer) getConfig("chickens", 5);
		for(int i=0; i <= chickenAmount; i++){
			Entity mob = p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.CHICKEN);
			mob.setVelocity(p.getEyeLocation().getDirection().multiply(2));
		}
		return true;
	}
}
