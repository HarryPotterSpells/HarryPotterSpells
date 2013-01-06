package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="WingardiumLeviosa",
		description="Makes the caster fly for a short while or until next cast",
		range=0,
		goThroughWalls=false
)
public class WingardiumLeviosa extends Spell{
	
	public WingardiumLeviosa(HarryPotterSpells instance) {
		super(instance);
	}

	public void cast(final Player p) {
		if(p.isFlying()) {
			p.setFlying(false);
			p.setAllowFlight(false);
		} else {
			p.setAllowFlight(true);
			p.setFlying(true);
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				   public void run() {
					   if(p.isFlying()) {
						   p.setFlying(false);
						   p.setAllowFlight(false);
					   } 
				   }
				}, 300L);
		}
	}

}
