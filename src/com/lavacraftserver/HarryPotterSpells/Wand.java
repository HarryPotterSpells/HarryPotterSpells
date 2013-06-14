package com.lavacraftserver.HarryPotterSpells;

import org.bukkit.inventory.ItemStack;

/**
 * This class manages the wand
 */
public class Wand {

    /**
     * Checks if a given {@link ItemStack} is useable as a wand
     * @param i the itemstack
     * @return {@code true} if the ItemStack is useable as a wand
     */
	public boolean isWand(ItemStack i) {
		return i.getTypeId() == HPS.Plugin.getConfig().getInt("wand-id", 280);
	}
	
	/**
	 * Gets the wand
	 * @return an {@link ItemStack} that has been specified as a wand in the config
	 */
	public ItemStack getWand() {
		return new ItemStack(HPS.Plugin.getConfig().getInt("wand-id", 280));
	}

}
