package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.lavacraftserver.HarryPotterSpells.HPS;

public class Wand {

	public boolean isWand(ItemStack i) {
		if(i.getTypeId() == HPS.Plugin.getConfig().getInt("wand-id", 280)) {
			if(HPS.Plugin.getConfig().getBoolean("apply-wand-enchantment", false)) {
				Map<Enchantment, Integer> wand = new HashMap<Enchantment, Integer>();
				wand.put(Enchantment.DURABILITY, 1);
				Map<Enchantment, Integer> item = (HashMap<Enchantment, Integer>)i.getEnchantments();
				if(wand == item) {
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	public ItemStack getWand() {
		ItemStack wand = new ItemStack(HPS.Plugin.getConfig().getInt("wand-id", 280));
		if(HPS.Plugin.getConfig().getBoolean("apply-wand-enchantment", false)) {
			wand.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		}
		return wand;
	}

}
