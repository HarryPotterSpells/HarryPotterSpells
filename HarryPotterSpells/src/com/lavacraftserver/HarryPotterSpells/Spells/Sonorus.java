package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.PM;
import com.lavacraftserver.HarryPotterSpells.Utils.MiscListeners;

public class Sonorus {
	
	public static void cast(final Player p) {
		MiscListeners.sonorus.put(p.getName(), true);
		PM.hps.getServer().getScheduler().scheduleSyncDelayedTask(PM.hps, new Runnable() {
			   public void run() {
				   if(MiscListeners.sonorus.containsKey(p.getName())) {
					   MiscListeners.sonorus.remove(p.getName());
				   } 
			   }
			}, 400L);
	}

}
