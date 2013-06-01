package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Enderchest",
		description="Open your personal enderchest.",
		range=10,
		goThroughWalls=false
		)
public class EnderChestSpell extends Spell{
	
	@Override
	public boolean cast(Player p) {
		Inventory i = p.getEnderChest();
		p.openInventory(i);
		return true;
	}

}
