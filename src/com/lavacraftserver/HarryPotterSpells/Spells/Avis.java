package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.SpellInfo;

@SpellInfo (
		name = "Avis",
		description = "descAvis",
		range = 0,
		goThroughWalls = false,
		cooldown=300
)
public class Avis extends Spell {
    
    public Avis(HPS instance) {
        super(instance);
    }

    public boolean cast(Player p) {
		int chickenAmount = (Integer) getConfig("chickens.amount", 5), batAmount = (Integer) getConfig("bats.amount", 0), chickenVelocity = (Integer) getConfig("chickens.velocity", 2), batVelocity = (Integer) getConfig("bats.velocity", 2);
		for(int i = 0; i <= chickenAmount; i++) {
			Entity mob = p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.CHICKEN);
			mob.setVelocity(p.getEyeLocation().getDirection().multiply(chickenVelocity));
		}
		for(int i = 0; i <= batAmount; i++) {
		    Entity mob = p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.BAT);
		    mob.setVelocity(p.getEyeLocation().getDirection().multiply(batVelocity));
		}
		return true;
	}
}
