package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Episkey",
		description="Slowly heals your target",
		range=50,
		goThroughWalls=false
)
public class Episkey extends Spell {
	public Episkey(HarryPotterSpells instance) {
		super(instance);
	}

	public void cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof LivingEntity) {
			LivingEntity livingentity = Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			livingentity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600, 1));
		} else {
			plugin.PM.warn(p, "You can only cast this on a player or mob!");
		}
	}
}
