package com.lavacraftserver.HarryPotterSpells;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * This class manages the wand
 */
public class Wand {
    private HPS HPS;
    
    /**
     * Constructs the {@link Wand}
     * @param plugin an instance of {@link HPS}
     */
    public Wand(HPS plugin) {
        HPS = plugin;
    }

    /**
     * Checks if a given {@link ItemStack} is useable as a wand
     * @param i the itemstack
     * @return {@code true} if the ItemStack is useable as a wand
     */
	public boolean isWand(ItemStack i) {
		if(i.getTypeId() == HPS.getConfig().getInt("wand-id", 280)) {
			if(HPS.getConfig().getBoolean("apply-wand-enchantment", false)) {
				Map<Enchantment, Integer> wand = new HashMap<Enchantment, Integer>();
				wand.put(Enchantment.DURABILITY, 1);
				Map<Enchantment, Integer> item = (HashMap<Enchantment, Integer>)i.getEnchantments();
				return wand == item;
			} else
				return true;
		} else
			return false;
	}
	
	/**
	 * Gets the wand
	 * @return an {@link ItemStack} that has been specified as a wand in the config
	 */
	public ItemStack getWand() {
		return new ItemStack(HPS.getConfig().getInt("wand-id", 280));
	}

}
