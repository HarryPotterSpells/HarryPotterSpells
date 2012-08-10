package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class Sonorus extends Spell {
	
	public Sonorus(HarryPotterSpells instance) {
		super(instance);
	}

	public  void cast(final Player p) {
		plugin.MiscListeners.sonorus.add(p.getName());
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			   public void run() {
				   if(plugin.MiscListeners.sonorus.contains(p.getName())) {
					   plugin.MiscListeners.sonorus.remove(p.getName());
				   } 
			   }
			}, 400L);
	}

}
