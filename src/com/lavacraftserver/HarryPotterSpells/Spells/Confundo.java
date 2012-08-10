package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

public class Confundo extends Spell {
	
	public Confundo(HarryPotterSpells instance) {
		super(instance);
	}

	public void cast(Player p) {
		if(Targeter.getTarget(p, 50) instanceof Player) {
			Player player = (Player) Targeter.getTarget(p, 50);
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 1));
		} else {
			plugin.PM.warn(p, "You can only cast this on a player!");
		}
	}
	
}
