package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.PM;

public class Evanesco {
	
	public static void cast(final Player p) {
		for (Player players : PM.hps.getServer().getOnlinePlayers()) {
            players.hidePlayer(p);
            p.getWorld().createExplosion(p.getLocation(), 0, false);
        }
		PM.hps.getServer().getScheduler().scheduleSyncDelayedTask(PM.hps, new Runnable() {
			   public void run() {
				   for (Player players : PM.hps.getServer().getOnlinePlayers()) {
			            players.showPlayer(p);
			       }
				   p.getWorld().createExplosion(p.getLocation(), 0, false);
			   }
			}, 300L);
	}

}
