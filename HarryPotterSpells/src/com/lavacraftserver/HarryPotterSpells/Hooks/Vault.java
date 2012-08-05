package com.lavacraftserver.HarryPotterSpells.Hooks;

import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.lavacraftserver.HarryPotterSpells.PM;

public class Vault extends JavaPlugin {
	public static Permission perm = null;
	public static Economy econ = null;
	
	public static void setupVault() {
		if(PM.hps.getConfig().getBoolean("VaultEnabled") != true) {
			return;
		}
		if(Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
			PM.log("Hooked into Vault", Level.INFO);
			if(!setupEconomy()) {
				PM.log("Economy plugin not found. Economy features have been disabled", Level.WARNING);
			} else {
				PM.log("Hooked into " + econ.getName(), Level.INFO);
			}
			if(setupPermissions()) {
				PM.log("Hooked into " + perm.getName(), Level.INFO);
			}
    	} else {
    		PM.hps.getConfig().set("VaultEnabled", false);
    		PM.log("Could not hook into Vault. Economy features have been disabled.", Level.WARNING);
    		return;
    	}
	}
	
	public static boolean setupPermissions() {
    	RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
    	if (rsp == null) {
            return false;
        }
    	perm = rsp.getProvider();
        return perm != null;
    }
    
    public static boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

}
