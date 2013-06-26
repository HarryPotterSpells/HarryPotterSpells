package com.hpspells.core.spell;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.Targeter;

@SpellInfo (
		name="Finite Incantatum",
		description="descFiniteIncantatem",
		range=50,
		goThroughWalls=false,
		cooldown=45
)
public class FiniteIncantatem extends Spell {
    
    public FiniteIncantatem(HPS instance) {
        super(instance);
    }

    @Override
	public boolean cast(Player p) {
		if (Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			Player target = (Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			PetrificusTotalus.players.remove(target.getName());
			
			for (PotionEffect effect : target.getActivePotionEffects()) {
				target.removePotionEffect(effect.getType());
			}
			
			Location loc = new Location(target.getWorld(), target.getLocation().getBlockX(), target.getLocation().getBlockY() + 1, target.getLocation().getBlockZ());
			target.getWorld().createExplosion(loc, 0F);
			
			return true;
		} else {
			HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
			return false;
		}
	}
}

