package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

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
		EntityType et;
		int maxMobs = 17, minMobs = 1;
		int randomNum = new Random().nextInt(maxMobs - minMobs + 1) + minMobs;
		
		switch(randomNum) {
		case 1:	et = EntityType.CHICKEN;
				break;
		case 2:	et = EntityType.COW;
				break;
		case 3: et = EntityType.MUSHROOM_COW;
				break;
		case 4:	et = EntityType.OCELOT;
				break;
		case 5:	et = EntityType.PIG;
				break;
		case 6:	et = EntityType.SHEEP;
				break;
		case 7: et = EntityType.WOLF;
				break;
		case 8:	et = EntityType.PIG_ZOMBIE;
				break;
		case 9:	et = EntityType.CAVE_SPIDER;
				break;
	    case 10: et = EntityType.CREEPER;
				 break;
		case 11: et = EntityType.MAGMA_CUBE;
				 break;
		case 12: et = EntityType.SILVERFISH;
				 break;
		case 13: et = EntityType.SKELETON;
				 break;
		case 14: et = EntityType.SLIME;
				 break;
		case 15: et = EntityType.SPIDER;
				 break;
		case 16: et = EntityType.ZOMBIE;
				 break;
		case 17: et = EntityType.GIANT;
				 break;
		default: et = EntityType.CHICKEN;
				 break;
		}
		return et;
	}

}
