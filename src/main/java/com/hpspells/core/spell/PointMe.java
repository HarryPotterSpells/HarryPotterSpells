package com.hpspells.core.spell;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo (
		name="Point Me",
		description="descPointMe",
		range=10,
		goThroughWalls=false,
		cooldown=0,
		icon=Material.COMPASS
)
public class PointMe extends Spell{
	    
    public PointMe(HarryPotterSpells instance) {
        super(instance);
    }
	
	public boolean cast(Player p) {
		Location loc = p.getLocation();
		loc.setYaw(180F);
		loc.setPitch(0);
		p.teleport(loc);
		
		return true;
	}

}
