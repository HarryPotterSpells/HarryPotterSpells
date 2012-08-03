package com.lavacraftserver.HarryPotterSpells.Spells;

//import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.inventory.ItemStack;

import com.lavacraftserver.HarryPotterSpells.PM;
import com.lavacraftserver.HarryPotterSpells.Spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

public class Expelliarmus {

	public static void cast(Player p) {
		SmallFireball sf = p.launchProjectile(SmallFireball.class);
		sf.setBounce(false);
		sf.setShooter(p);
		sf.setYield(0);
		Targeter.NeedsTargeted.put(sf.getUniqueId(), Spell.EXPELLIARMUS);
	}
	
	public static void hit(Entity e) {
		if(e instanceof Player) {
			Player player = (Player)e;
			
			Location playerloc = player.getLocation();
			//We need to get the direction the player is facing, add 2, and drop it there.
			
			ItemStack wand = new ItemStack(Material.STICK, 1);
			ItemStack wsword = new ItemStack(Material.WOOD_SWORD, 1);
			ItemStack ssword = new ItemStack(Material.STONE_SWORD, 1);
			ItemStack isword = new ItemStack(Material.IRON_SWORD, 1);
			ItemStack gsword = new ItemStack(Material.GOLD_SWORD, 1);
			ItemStack dsword = new ItemStack(Material.DIAMOND_SWORD, 1);
			ItemStack bow = new ItemStack(Material.BOW, 1);
			
			if (player.getItemInHand().getType().equals(wand)) {
				player.getWorld().dropItem(playerloc, wand);
			}
			if (player.getItemInHand().getType().equals(wsword)) {
				player.getWorld().dropItem(playerloc, wsword);
			}
			if (player.getItemInHand().getType().equals(ssword)) {
				player.getWorld().dropItem(playerloc, ssword);
			}
			if (player.getItemInHand().getType().equals(isword)) {
				player.getWorld().dropItem(playerloc, isword);
			}
			if (player.getItemInHand().getType().equals(gsword)) {
				player.getWorld().dropItem(playerloc, gsword);
			}
			if (player.getItemInHand().getType().equals(dsword)) {
				player.getWorld().dropItem(playerloc, dsword);
			}
			if (player.getItemInHand().getType().equals(bow)) {
				player.getWorld().dropItem(playerloc, bow);
			}
			
			if (PM.hps.getConfig().getBoolean("shout-on-spell-use") == true) {
				//caster.chat("Expelliarmus!");
			}
			if (PM.hps.getConfig().getBoolean("notify-on-spell-use") == true) {
				//caster.sendMessage(ChatColor.AQUA + "You have cast Expelliarmus!");
			}
		} else {
			//caster.sendMessage(ChatColor.RED + "You must be targetting a player!");
		}
	}
}
