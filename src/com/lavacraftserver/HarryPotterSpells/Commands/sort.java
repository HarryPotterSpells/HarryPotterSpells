package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class sort extends Executor{
	
	public sort(HarryPotterSpells instance){
		super(instance);
	}
	
	@Override
	public String getCommand() {
		return "sort";
	}
	
	public void group(Player p) {
		int houseNumber = new Random().nextInt(4 - 1 + 1) + 1;
		String house, group, player;
		switch(houseNumber) {
			case 1: house = ChatColor.DARK_RED + "Gryffindor";
					group = "Gryffindor";
					player = ChatColor.GOLD + "";
					break;
			case 2: house = ChatColor.BLACK + "Hufflepuff";
					group = "Hufflepuff";
					player = ChatColor.YELLOW + "";
					break;
			case 3: house = ChatColor.DARK_BLUE + "Ravenclaw";
				    group = "Ravenclaw";
					player = ChatColor.GRAY + "";
					break;
			case 4: house = ChatColor.GRAY + "Slytherin";
					group = "Slytherin";
					player = ChatColor.DARK_GREEN + "";
					break;
			default: house = ChatColor.BLACK + "Hufflepuff";
				     group = "Hufflepuff";
					 player = ChatColor.YELLOW + "";
					 break;
		}
		player += p.getName();
		Bukkit.getServer().broadcastMessage(player + ChatColor.WHITE + " has been sorted into... " + house + "!");
		plugin.Vault.perm.playerAddGroup(p, group);
	}
	
	public void perm(Player p) {
		int houseNumber = new Random().nextInt(4 - 1 + 1) + 1;
		String house, perm = "HarryPotterSpells.house.", player;
		switch(houseNumber) {
			case 1: house = ChatColor.DARK_RED + "Gryffindor";
					perm += "gryffindor";
					player = ChatColor.GOLD + "";
					break;
			case 2: house = ChatColor.BLACK + "Hufflepuff";
					perm += "hufflepuff";
					player = ChatColor.YELLOW + "";
					break;
			case 3: house = ChatColor.DARK_BLUE + "Ravenclaw";
					perm += "ravenclaw";
					player = ChatColor.GRAY + "";
					break;
			case 4: house = ChatColor.GRAY + "Slytherin";
					perm += "slytherin";
					player = ChatColor.DARK_GREEN + "";
					break;
			default: house = ChatColor.BLACK + "Hufflepuff";
					 perm += "hufflepuff";
					 player = ChatColor.YELLOW + "";
					 break;
		}
		player += p.getName();
		plugin.getServer().broadcastMessage(player + ChatColor.WHITE + " has been sorted into... " + house + "!");
		plugin.Vault.perm.playerAdd(p, perm);
	}


	public void runPlayer(Player sender, String[] args) {
		Player p = (Player)sender;
		if(plugin.getConfig().getBoolean("SortingHat.addHousePermissionNodes")) {
			if (p.hasPermission("HarryPotterSpells.house.gryffindor") || p.hasPermission("HarryPotterSpells.house.hufflepuff") || p.hasPermission("HarryPotterSpells.house.ravenclaw") || p.hasPermission("HarryPotterSpells.house.slytherin")) {
				plugin.PM.warn((Player)p, "You have already been sorted.");
			} else {
				perm(p);
			}
			return;
		}
		if(plugin.getConfig().getBoolean("SortingHat.addHouseGroups")) {
			if(p.hasPermission("HarryPotterSpells.house.gryffindor") || p.hasPermission("HarryPotterSpells.house.hufflepuff") || p.hasPermission("HarryPotterSpells.house.ravenclaw") || p.hasPermission("HarryPotterSpells.house.slytherin")) {
				plugin.PM.warn(p, "You have already been sorted.");
			} else {
				group(p);
			}
			return;
		}
		
	}//

	
	
}
