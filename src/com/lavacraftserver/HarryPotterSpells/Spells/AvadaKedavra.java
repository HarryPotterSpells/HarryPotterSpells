package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="AvadaKedavra",
		description="Kills whomever is targeted",
		range=50,
		goThroughWalls=false
)
public class AvadaKedavra extends Spell {

	public void cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof LivingEntity) {
			LivingEntity livingentity = Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			livingentity.setHealth(0);
		} else {
			HPS.PM.warn(p, "This can only be used on a player or mob.");
		}
	}
	
}
