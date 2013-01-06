package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Deprimo",
		description="Slows your target to an almost halt",
		range=20,
		goThroughWalls=false
)
public class Deprimo extends Spell{
	
	public Deprimo(HarryPotterSpells instance) {
		super(instance);
	}

	public void cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			final Player target = (Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 500, 1));
			plugin.MiscListeners.deprimo.add(target.getName());
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				   public void run() {
					   plugin.MiscListeners.deprimo.remove(target.getName());
				   }
				}, 400L);
		} else {
			plugin.PM.warn(p, "You can only cast this on a player!");
		}
	}

}
