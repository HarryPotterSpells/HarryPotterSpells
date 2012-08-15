package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

public class PetrificusTotalus extends Spell {

	public PetrificusTotalus(HarryPotterSpells instance) {
		super(instance);
	}

	@Override
	public void cast(Player p) {
		if (Targeter.getTarget(p, 50) instanceof LivingEntity) {
			plugin.MiscListeners.petrificustotalus.add(p.getName());
		} else {
			plugin.PM.warn(p, "This may only be used on a player or a mob.");
		}
	}

}
