package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell(
		name="Accio",
		description="Pulls surrounding drops towards you",
		goThroughWalls=false,
		cooldown=5
)
public class Accio extends Spell {
	
	public Accio(HPS plugin) {
        super(plugin);
    }

    @Override
	public boolean cast(Player p) {
		int radius = (Integer) getConfig("radius", 5);
		boolean worked = false;
		
		for(Entity entity : p.getNearbyEntities(radius, radius, radius)) {
			if(entity instanceof Item) {
				Item item = (Item) entity;
				worked = true;
				
				// Set the items velocity to the difference between the item and the player's body then divide it by 5 to prevent it from moving too quickly/powerfully
				Vector newVelocity = p.getLocation().add(0,1,0).toVector().subtract(item.getLocation().toVector()).divide(new Vector(5,5,5));
				item.setVelocity(newVelocity);
			}
			
		}
		
		return worked;
	}

}
