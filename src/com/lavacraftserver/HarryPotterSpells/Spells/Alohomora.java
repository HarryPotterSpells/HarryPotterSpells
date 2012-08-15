package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class Alohomora extends Spell {

	public Alohomora(HarryPotterSpells instance) {
		super(instance);
	}

	@Override
	public void cast(Player p) {
		Block b = p.getTargetBlock(null, 50);
		if (isDoor(b)) {
			((Door) b.getState().getData()).setOpen(true);
		} else {
			plugin.PM.warn(p, "This spell can only be used on doors.");
		}
		return;

	}
	
	public boolean isDoor(Block b) {
		switch (b.getTypeId()) {
		case 64: return true;
		case 71: return true;
		default: return false;
		}
	}

}
