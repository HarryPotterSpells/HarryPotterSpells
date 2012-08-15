package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

public class Obscuro extends Spell {

	public Obscuro(HarryPotterSpells instance) {
		super(instance);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cast(Player p) {
		if (Targeter.getTarget(p, 50) instanceof LivingEntity) {
			LivingEntity le = Targeter.getTarget(p, 50);
			le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 10));
		} else {
			plugin.PM.warn(p, "This may only be used on a player or a mob.");
		}
	}

}
