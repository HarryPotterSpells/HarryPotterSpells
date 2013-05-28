package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Spongify",
		description="Prevents fall damage for 30 seconds",
		range=0,
		goThroughWalls=false
)
public class Spongify extends Spell {

	public  void cast(final Player p) {
		HarryPotterSpells.MiscListeners.spongify.add(p.getName());
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HarryPotterSpells.Plugin, new Runnable() {
			   public void run() {
				   if(HarryPotterSpells.MiscListeners.spongify.contains(p.getName())) {
					   HarryPotterSpells.MiscListeners.spongify.remove(p.getName());
				   } 
			   }
			}, 200L);
	}

}
