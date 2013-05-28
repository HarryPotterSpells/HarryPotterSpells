package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Conjunctivitis",
		description="Blinds your target",
		range=20,
		goThroughWalls=false
)
public class Conjunctivitis extends Spell {

	public void cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			Player player = (Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 600, 1));
		} else {
			HPS.PM.warn(p, "You can only cast this on a player!");
		}
	}
}

