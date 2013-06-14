package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Enderchest",
		description="descEnderChest",
		range=10,
		goThroughWalls=false,
		cooldown=0,
		icon=Material.ENDER_CHEST
		)
public class EnderChestSpell extends Spell{
	
	@Override
	public boolean cast(Player p) {
		Inventory i = p.getEnderChest();
		p.openInventory(i);
		return true;
	}

}
