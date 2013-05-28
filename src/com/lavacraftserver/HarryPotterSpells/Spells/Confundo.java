package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
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
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 1));
		} else {
			HarryPotterSpells.PM.warn(p, "You can only cast this on a player!");
		}
	}
	
}
