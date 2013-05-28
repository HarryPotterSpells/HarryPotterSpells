package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Spongify",
		description="Prevents fall damage for 30 seconds",
		range=0,
		goThroughWalls=false
)
public class Spongify extends Spell {

	public  void cast(final Player p) {
		HPS.MiscListeners.spongify.add(p.getName());
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS.Plugin, new Runnable() {
			   public void run() {
				   if(HPS.MiscListeners.spongify.contains(p.getName())) {
					   HPS.MiscListeners.spongify.remove(p.getName());
				   } 
			   }
			}, 200L);
	}

}
