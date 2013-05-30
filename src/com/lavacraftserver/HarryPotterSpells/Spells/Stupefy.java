package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Stupefy",
		description="Stuns the target",
		range=50,
		goThroughWalls=false
)
public class Stupefy extends Spell {

	@Override
	public void cast(Player p) {
		if (Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof LivingEntity) {
			LivingEntity le = Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			
			int confusionDuration = HPS.Plugin.getConfig().getInt("spells.stupefy.confusion-duration");
			int weaknessDuration = HPS.Plugin.getConfig().getInt("spells.stupefy.weakness-duration");
			int knockback = HPS.Plugin.getConfig().getInt("spells.stupefy.knockback");

			le.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, confusionDuration, 1));
			le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, weaknessDuration, 1));
			Vector unitVector = le.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
			le.setVelocity(unitVector.multiply(knockback));
		} else {
			HPS.PM.warn(p, "This can only be used on a player or a mob.");
		}
	}
}