package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Reparo",
		description="Repairs the item in your hand fully",
		range=0,
		goThroughWalls=false
)
public class Reparo extends Spell {

	@Override
	public void cast(Player p) {
		repairItems(p.getInventory().getContents(), p);
	}
	
	private void repairItem(final ItemStack item) {
		final Material material = Material.getMaterial(item.getTypeId());
		if (material.isBlock() || material.getMaxDurability() < 1) {
			return;
		}
		item.setDurability((short) 0);
	}
	
	private void repairItems(ItemStack[] itemstack, Player p) {
		for (ItemStack item : itemstack) {
			if (item == null) {
				continue;
			}
			repairItem(item);
		}
	}
}
