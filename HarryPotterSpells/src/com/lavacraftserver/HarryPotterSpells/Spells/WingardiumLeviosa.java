package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.PM;

public class WingardiumLeviosa {
	
	public static void cast(final Player p) {
		if(p.isFlying()) {
			p.setFlying(false);
		} else {
			p.setFlying(true);
			PM.hps.getServer().getScheduler().scheduleSyncDelayedTask(PM.hps, new Runnable() {
				   public void run() {
				       p.setFlying(false);
				   }
				}, 300L);
		}
	}

}
