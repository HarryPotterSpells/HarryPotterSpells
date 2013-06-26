package com.hpspells.core.spell;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.Targeter;

@SpellInfo (
		name="Orchideous",
		description="descOrchideous",
		range=50,
		goThroughWalls=false,
		cooldown=45,
		icon=Material.RED_ROSE
)
public class Orchideous extends Spell {
    
    public Orchideous(HPS instance) {
        super(instance);
    }

    @Override
	public boolean cast(Player p) {
		Block b = p.getTargetBlock(Targeter.getTransparentBlocks(), this.getRange());
		if (isValidBlock(b) && blockAboveIsValidBlock(b)) {
			getBlockAbove(b).setType(Material.RED_ROSE);
			return true;
		} else {
			HPS.PM.warn(p, HPS.Localisation.getTranslation("spellNoRose"));
			return false;
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
