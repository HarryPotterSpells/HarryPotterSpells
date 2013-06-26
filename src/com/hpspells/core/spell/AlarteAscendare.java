package com.hpspells.core.spell;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.Targeter;

@SpellInfo (
		name="Alarte Ascendare",
		description="descAlarteAscendare",
		range=30,
		goThroughWalls=false,
		cooldown=45
)
public class AlarteAscendare extends Spell {

    public AlarteAscendare(HPS instance) {
        super(instance);
    }

    @Override
	public boolean cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			le.setVelocity(new Vector(0,1,0));
		}
		return true;
	}

}
