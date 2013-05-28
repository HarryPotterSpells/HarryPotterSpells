package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Deprimo",
		description="Slows your target to an almost halt",
		range=20,
		goThroughWalls=false
)
public class Deprimo extends Spell{

	public void cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			final Player target = (Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 500, 1));
			HPS.MiscListeners.deprimo.add(target.getName());
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS.Plugin, new Runnable() {
				   public void run() {
					   HPS.MiscListeners.deprimo.remove(target.getName());
				   }
				}, 400L);
		} else {
			HPS.PM.warn(p, "You can only cast this on a player!");
		}
	}

}
