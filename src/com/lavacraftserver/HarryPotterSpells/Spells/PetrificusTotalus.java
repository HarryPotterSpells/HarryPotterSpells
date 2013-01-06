package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Petrificus Totalus",
		description="Stuns the target player",
		range=50,
		goThroughWalls=false
)
public class PetrificusTotalus extends Spell {

	public PetrificusTotalus(HarryPotterSpells instance) {
		super(instance);
	}

	@Override
	public void cast(Player p) {
		if (Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			plugin.MiscListeners.petrificustotalus.add(((Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls())).getName());
		} else {
			plugin.PM.warn(p, "This may only be used on a player or a mob.");
		}
	}

}
