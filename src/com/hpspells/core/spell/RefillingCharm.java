package com.hpspells.core.spell;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo (
		name="Refilling Charm",
		description="descRefillingCharm",
		range=0,
		goThroughWalls=false,
		cooldown=120
)

public class RefillingCharm extends Spell{

	public RefillingCharm(HPS instance) {
		super(instance);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean cast(Player p) {
		Inventory inventory = p.getInventory();
		ItemStack[] contents = inventory.getContents();
		//Loop through the inventory
		for (ItemStack is : contents){
			// If the item stack is a bucket
			if (is.getType() == Material.BUCKET){
				inventory.remove(Material.BUCKET);
				//Gets string from config , weather to refill water or milk
				String refill = (String) getConfig("bucket", "water");
				if (refill.equals("water")){
					inventory.addItem(new ItemStack(Material.WATER_BUCKET));
				}
				if (refill.equals("milk")){
					inventory.addItem(new ItemStack(Material.MILK_BUCKET));
				}
			}
			// If the item stack is a bowl
			if (inventory.contains(Material.BOWL)){
				inventory.remove(Material.BOWL);
				inventory.addItem(new ItemStack(Material.MUSHROOM_SOUP));
			}
		}
		//Update inventory (NOTE , Only way to update inventory without clicking on the item stack)
		p.updateInventory();
		return true;
	}

}
