package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.PM;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

public class Confundo {
	
	public static void cast(Player p) {
		if(Targeter.getTarget(p, 50) instanceof Player) {
			Player player = (Player) Targeter.getTarget(p, 50);
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 1));
		} else {
			PM.warn(p, "You can only cast this on a player!");
		}
	}
	
}
