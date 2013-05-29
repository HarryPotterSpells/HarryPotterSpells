package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="AlarteAscendare",
		description="Shoots a random mob from your wand",
		range=0,
		goThroughWalls=false
)
public class AlarteAscendare extends Spell {

	public void cast(Player p) {
		Entity mob = p.getWorld().spawnEntity(p.getEyeLocation(), randomEntity());
		if (mob == null){
			return;
		}
		mob.setVelocity(p.getEyeLocation().getDirection().multiply(2));
	}
	
	public EntityType randomEntity() {
		@SuppressWarnings("unchecked")
		List<String> mobs = (List<String>) HPS.Plugin.getConfig().getList("spells.alarteascendare.mobs");
		if (mobs == null){
			return EntityType.PIG;
		}
		int randomNum = new Random().nextInt(mobs.size());
		return EntityType.fromName(mobs.get(randomNum));
	}

}
