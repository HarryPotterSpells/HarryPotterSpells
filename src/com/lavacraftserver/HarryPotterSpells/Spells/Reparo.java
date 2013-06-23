package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.SpellInfo;

@SpellInfo (
		name="Reparo",
		description="descReparo",
		range=0,
		goThroughWalls=false,
		cooldown=14400
)
public class Reparo extends Spell {
    
    public Reparo(HPS instance) {
        super(instance);
    }

    @Override
	public boolean cast(Player p) {
		repairItems(p.getInventory().getContents(), p);
		repairItems(p.getEquipment().getArmorContents(), p);
		Location loc = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ());
		p.getWorld().createExplosion(loc, 0F);
		return true;
	}
	
	private void repairItem(final ItemStack item) {
		final Material material = Material.getMaterial(item.getTypeId());
		if (material.isBlock() || material.getMaxDurability() < 1)
			return;
		item.setDurability((short) 0);
	}
	
	private void repairItems(ItemStack[] itemstack, Player p) {
		for (ItemStack item : itemstack) {
			if (item == null)
				continue;
			repairItem(item);
		}
	}
	
}
