package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Confundo",
		description="Confuses your target",
		range=20,
		goThroughWalls=false
)
public class Confundo extends Spell {

	public void cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			Player player = (Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			int duration = HPS.Plugin.getConfig().getInt("spells.confundo.duration");
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration, 1));
		} else {
			HPS.PM.warn(p, "This can only be used on a player.");
		}
	}
	
}
