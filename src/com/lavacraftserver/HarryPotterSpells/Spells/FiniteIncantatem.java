package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Finite Incantatum",
		description="Removes potion effects and unpetrifies the target player",
		range=50,
		goThroughWalls=false
)
public class FiniteIncantatem extends Spell {
	
	@Override
	public void cast(Player p) {
		if (Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			Player target = (Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			PetrificusTotalus.players.remove(target.getName());
			
			for (PotionEffect effect : target.getActivePotionEffects()) {
				target.removePotionEffect(effect.getType());
			}
			
			Location loc = new Location(target.getWorld(), target.getLocation().getBlockX(), target.getLocation().getBlockY() + 1, target.getLocation().getBlockZ());
			target.getWorld().createExplosion(loc, 0F);
		} else {
			HPS.PM.warn(p, "This can only be used on a player.");
		}
	}
}

