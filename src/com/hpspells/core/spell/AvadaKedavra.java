package com.hpspells.core.spell;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.Targeter;

@SpellInfo (
		name="Avada Kedavra",
		description="descAvadaKedavra",
		range=50,
		goThroughWalls=false,
		cooldown=300
)
public class AvadaKedavra extends Spell {
    
    public AvadaKedavra(HPS instance) {
        super(instance);
    }

    public boolean cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof LivingEntity) {
			LivingEntity livingentity = Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			livingentity.setHealth(0);
			return true;
		} else {
			HPS.PM.warn(p, HPS.Localisation.getTranslation("spellLivingEntityOnly"));
			return false;
		}
	}
	
}
