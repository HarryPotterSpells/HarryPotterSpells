package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Jobs.ClearJob;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Sonorus",
		description="Broadcasts what ever you next shout to the whole server",
		range=0,
		goThroughWalls=false
)
public class Sonorus extends Spell implements ClearJob {

	public  void cast(final Player p) {
		HarryPotterSpells.MiscListeners.sonorus.add(p.getName());
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HarryPotterSpells.Plugin, new Runnable() {
			   public void run() {
				   if(HarryPotterSpells.MiscListeners.sonorus.contains(p.getName())) {
					   HarryPotterSpells.MiscListeners.sonorus.remove(p.getName());
				   } 
			   }
			}, 400L);
	}

	@Override
	public void clear() {
		HarryPotterSpells.MiscListeners.sonorus.clear();
	}

}
