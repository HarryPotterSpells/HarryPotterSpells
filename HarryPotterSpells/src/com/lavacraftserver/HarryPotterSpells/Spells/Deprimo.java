package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.PM;
import com.lavacraftserver.HarryPotterSpells.Utils.MiscListeners;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

public class Deprimo {
	
	public static void cast(Player p) {
		if(Targeter.getTarget(p, 20) instanceof Player) {
			final Player target = (Player) Targeter.getTarget(p, 20);
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 500, 1));
			MiscListeners.deprimo.add(target.getName());
			PM.hps.getServer().getScheduler().scheduleSyncDelayedTask(PM.hps, new Runnable() {
				   public void run() {
					   MiscListeners.deprimo.remove(target.getName());
				   }
				}, 400L);
		} else {
			PM.warn(p, "You can only cast this on a player!");
		}
	}

}
