package com.lavacraftserver.HarryPotterSpells.SortingHat;

import java.util.Random;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SortingHat extends JavaPlugin {

    public static Permission perms = null;
	
	public static boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("sort")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("[SortingHat] You must be a player to be sorted!");
			} else {
				if (getConfig().getBoolean("add-house-permission-nodes") == true) {
					if (sender.hasPermission("house.gryffindor") || sender.hasPermission("house.hufflepuff") || sender.hasPermission("house.ravenclaw") || sender.hasPermission("house.slytherin")) {
						sender.sendMessage(ChatColor.RED + "You have already been sorted!");
						return true;
					} else {
						Player p = (Player)sender;
						Random generator = new Random();
			        
						int minValue = 1;
						int maxValue = 4;
			        
						int houseNumber = generator.nextInt(maxValue - minValue + 1) + minValue;
			        
						if (houseNumber == 1) {
							getServer().broadcastMessage(ChatColor.GOLD + p.getName() + " has been sorted into... " + ChatColor.DARK_RED + "Gryffindor!");
							perms.playerAdd(p, "house.gryffindor");
							return true;
						}
						if (houseNumber == 2) {
							getServer().broadcastMessage(ChatColor.YELLOW + p.getName() + " has been sorted into... " + ChatColor.BLACK + "Hufflepuff!");
							perms.playerAdd(p, "house.hufflepuff");
							return true;
						}
						if (houseNumber == 3) {
							getServer().broadcastMessage(ChatColor.GRAY + p.getName() + " has been sorted into... " + ChatColor.DARK_BLUE + "RavenClaw!");
							perms.playerAdd(p, "house.ravenclaw");
							return true;
						}
						if (houseNumber == 4) {
							getServer().broadcastMessage(ChatColor.DARK_GREEN + p.getName() + " has been sorted into... " + ChatColor.GRAY + "Slytherin!");
							perms.playerAdd(p, "house.slytherin");
							return true;
						}
					}
				}
				
				if (getConfig().getBoolean("add-into-house-groups") == true) {
					if (sender.hasPermission("house.gryffindor") || sender.hasPermission("house.hufflepuff") || sender.hasPermission("house.ravenclaw") || sender.hasPermission("house.slytherin")) {
						sender.sendMessage(ChatColor.RED + "You have already been sorted!");
						return true;
					} else {
						Player p = (Player)sender;
						Random generator = new Random();
			        
						int minValue = 1;
						int maxValue = 5;
			        
						int houseNumber = generator.nextInt(maxValue - minValue) + minValue;
			        
						if (houseNumber == 1) {
							getServer().broadcastMessage(ChatColor.GOLD + p.getName() + " has been sorted into... " + ChatColor.DARK_RED + "Gryffindor!");
							perms.playerAddGroup(p, "Gryffindor");
							return true;
						}
						if (houseNumber == 2) {
							getServer().broadcastMessage(ChatColor.YELLOW + p.getName() + " has been sorted into... " + ChatColor.BLACK + "Hufflepuff!");
							perms.playerAddGroup(p, "Hufflepuff");
							return true;
						}
						if (houseNumber == 3) {
							getServer().broadcastMessage(ChatColor.GRAY + p.getName() + " has been sorted into... " + ChatColor.DARK_BLUE + "RavenClaw!");
							perms.playerAddGroup(p, "Ravenclaw");
							return true;
						}
						if (houseNumber == 4) {
							getServer().broadcastMessage(ChatColor.DARK_GREEN + p.getName() + " has been sorted into... " + ChatColor.GRAY + "Slytherin!");
							perms.playerAddGroup(p, "Slytherin");
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
