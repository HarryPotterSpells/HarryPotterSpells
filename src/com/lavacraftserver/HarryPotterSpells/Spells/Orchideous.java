package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class Orchideous extends Spell {

	public Orchideous(HarryPotterSpells instance) {
		super(instance);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cast(Player p) {
		Block b = p.getTargetBlock(null, 50);
		if (isValidBlock(b) && blockAboveIsValidBlock(b)) {
			getBlockAbove(b).setType(Material.RED_ROSE);
		} else {
			plugin.PM.warn(p, "This spell cannot be cast on that block.");
		}

	}

	private boolean blockAboveIsValidBlock(Block b) {
		Block blockAbove = getBlockAbove(b);
		if (blockAbove == null) {
			return false;
		}
		if (blockAbove.getTypeId() == 0) {
			return true;
		}
		return false;
	}

	private Block getBlockAbove(Block b) {
		Location loc = b.getLocation();
		loc.setY(loc.getY() + 1);
		return loc.getBlock();
	}

	private boolean isValidBlock(Block b) {
		if (b == null) {
			return false;
		}
		switch (b.getTypeId()) {
		case 2: return true;
		case 3: return true;
		}
		return false;
	}
	
	
}
