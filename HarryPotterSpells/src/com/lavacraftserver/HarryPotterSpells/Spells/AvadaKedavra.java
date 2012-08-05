package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.PM;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;


public class AvadaKedavra {

	public static void cast(Player p) {
		if(Targeter.getTarget(p, 50) instanceof LivingEntity) {
			LivingEntity livingentity = Targeter.getTarget(p, 50);
			livingentity.setHealth(0);
		} else {
			PM.warn(p, "You can only cast this on a player or mob!");
		}
	}
	
}
