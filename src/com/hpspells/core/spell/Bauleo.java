package com.hpspells.core.spell;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;


public class Bauleo extends Spell {

	public Bauleo(com.hpspells.core.HPS instance) {
		super(instance);
	}

	@Override
	public boolean cast(Player p) {
		Chest chest = null;
		Location loc = p.getLocation();
		int radius = 5;
		outerLoop:
			for (int x = -(radius); x <= radius; x ++){
				for (int y = -(radius); y <= radius; y ++) {
					for (int z = -(radius); z <= radius; z ++) {
						Block b = new Location(p.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z).getBlock();
						if (b.getType() == Material.CHEST) {
							chest = (Chest) b;
							break outerLoop;
						}
					}
				}
			}
		if (chest == null) {
			return false;
		}
		for (Entity e : p.getNearbyEntities(5, 5, 5)) {
			if (e instanceof Item) {
				Item item = (Item) e;
				chest.getBlockInventory().addItem(item.getItemStack());
				item.remove();
			}
		}
		return true;
	}

}
