package com.hpspells.core.spell;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.Targeter;

@SpellInfo (
		name="Confundo",
		description="descConfundo",
		range=20,
		goThroughWalls=false,
		cooldown=180
)
public class Confundo extends Spell {
    
    public Confundo(HPS instance) {
        super(instance);
    }

    public boolean cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			Player player = (Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			long duration = getTime("duration", 200l);
			
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (int) duration, 1));
			return true;
		} else {
			HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
			return false;
		}
	}
	
}
