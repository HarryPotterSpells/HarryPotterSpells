package com.lavacraftserver.HarryPotterSpells.Hooks;

import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class Vault  {
	HarryPotterSpells plugin;
	public Vault(HarryPotterSpells instance){
		plugin=instance;
	}
	public Permission perm = null;
	public Economy econ = null;
	
	public void setupVault() {
		if(plugin.getConfig().getBoolean("VaultEnabled") != true) {
			return;
		}
		if(Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
			plugin.PM.log("Hooked into Vault", Level.INFO);
			if(!setupEconomy()) {
				plugin.PM.log("Economy plugin not found. Economy features have been disabled", Level.WARNING);
			} else {
				plugin.PM.log("Hooked into " + econ.getName(), Level.INFO);
			}
			if(setupPermissions()) {
				plugin.PM.log("Hooked into " + perm.getName(), Level.INFO);
			}
    	} else {
    		plugin.getConfig().set("VaultEnabled", false);
    		plugin.PM.log("Could not hook into Vault. Economy features have been disabled.", Level.WARNING);
    		return;
    	}
	}
	
	public boolean setupPermissions() {
    	RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
    	if (rsp == null) {
            return false;
        }
    	perm = rsp.getProvider();
        return perm != null;
    }
    
    public boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

}
