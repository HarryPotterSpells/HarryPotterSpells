package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.PM;
import com.lavacraftserver.HarryPotterSpells.Utils.MiscListeners;

public class Spongify {
	
	public static void cast(final Player p) {
		MiscListeners.spongify.put(p.getName(), true);
		PM.hps.getServer().getScheduler().scheduleSyncDelayedTask(PM.hps, new Runnable() {
			   public void run() {
				   if(MiscListeners.spongify.containsKey(p.getName())) {
					   MiscListeners.spongify.remove(p.getName());
				   } 
			   }
			}, 200L);
	}

}
