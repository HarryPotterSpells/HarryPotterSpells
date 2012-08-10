package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

public class Expelliarmus extends Spell {
	
	public Expelliarmus(HarryPotterSpells instance) {
		super(instance);
	}

	public void cast(Player p) {
		if(Targeter.getTarget(p, 25) instanceof Player) {
			Player target = (Player) Targeter.getTarget(p, 25);
			
			Location targetloc = target.getLocation();
			//We need to get the direction the target is facing, add 2, and drop it there.
			
			ItemStack wand = new ItemStack(Material.STICK, 1);
			ItemStack wsword = new ItemStack(Material.WOOD_SWORD, 1);
			ItemStack ssword = new ItemStack(Material.STONE_SWORD, 1);
			ItemStack isword = new ItemStack(Material.IRON_SWORD, 1);
			ItemStack gsword = new ItemStack(Material.GOLD_SWORD, 1);
			ItemStack dsword = new ItemStack(Material.DIAMOND_SWORD, 1);
			ItemStack bow = new ItemStack(Material.BOW, 1);
			
			if (target.getItemInHand().getType().equals(wand)) {
				target.getWorld().dropItem(targetloc, wand);
			}
			if (target.getItemInHand().getType().equals(wsword)) {
				target.getWorld().dropItem(targetloc, wsword);
			}
			if (target.getItemInHand().getType().equals(ssword)) {
				target.getWorld().dropItem(targetloc, ssword);
			}
			if (target.getItemInHand().getType().equals(isword)) {
				target.getWorld().dropItem(targetloc, isword);
			}
			if (target.getItemInHand().getType().equals(gsword)) {
				target.getWorld().dropItem(targetloc, gsword);
			}
			if (target.getItemInHand().getType().equals(dsword)) {
				target.getWorld().dropItem(targetloc, dsword);
			}
			if (target.getItemInHand().getType().equals(bow)) {
				target.getWorld().dropItem(targetloc, bow);
			}
		} else {
			plugin.PM.warn(p, "You can only cast this on players.");
		}
	}
}
