package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Avada Kedavra",
		description="descAvadaKedavra",
		range=50,
		goThroughWalls=false,
		cooldown=300
)
public class AvadaKedavra extends Spell {

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
