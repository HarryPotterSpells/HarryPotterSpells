package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.SpellInfo;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

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
