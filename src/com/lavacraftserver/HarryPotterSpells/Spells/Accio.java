package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;;

@spell(
		
		name="Accio",
		description="Pulls surrounding drops towards you",
		goThroughWalls=false
		
)

public class Accio extends Spell {

	public Integer radius = null;
	
	@Override
	public void cast(Player p) {
		
		if(radius == null) {
			
			radius = HPS.Plugin.getConfig().getInt("spells.accio.radius");
			
		}
		
		for(Entity entity : p.getNearbyEntities(radius, radius, radius)) {//gets nearby entities
			
			if(entity instanceof Item) {//if they're drops...
				
				Item item = (Item) entity;//cast them
				
				/*
				 * Set the items velocity to the difference between the item and the player's body
				 * then divide it by 5 to prevent it from moving too quickly/powerfully
				 */
				Vector newVelocity = p.getLocation().add(0,1,0).toVector().subtract(item.getLocation().toVector()).divide(new Vector(5,5,5));
				
				//apply the new velocity
				item.setVelocity(newVelocity);
				
			}
			
		}
		
	}

}
