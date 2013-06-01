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
		goThroughWalls=false,
		cooldown=180
)
public class Confundo extends Spell {

	public boolean cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			
			Player player = (Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			String durationString = HPS.Plugin.getConfig().getString("spells.confundo.duration", "200t");
			int duration = 0;
	
			if (durationString.endsWith("t")) {
				String ticks = durationString.substring(0, durationString.length() - 1);
				duration = Integer.parseInt(ticks);
			} else {
				duration = Integer.parseInt(durationString) * 20;
			}
			
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration, 1));
			return true;
	
		} else {
			HPS.PM.warn(p, "This can only be used on a player.");
			return false;
		}
	}
	
}
