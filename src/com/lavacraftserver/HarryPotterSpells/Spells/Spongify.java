package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class Spongify extends Spell {
	
	public Spongify(HarryPotterSpells instance) {
		super(instance);
	}

	public  void cast(final Player p) {
		plugin.MiscListeners.spongify.add(p.getName());
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			   public void run() {
				   if(plugin.MiscListeners.spongify.contains(p.getName())) {
					   plugin.MiscListeners.spongify.remove(p.getName());
				   } 
			   }
			}, 200L);
	}

}
