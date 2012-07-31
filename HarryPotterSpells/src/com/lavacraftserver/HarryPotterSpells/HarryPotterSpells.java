package com.lavacraftserver.HarryPotterSpells;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.lavacraftserver.HarryPotterSpells.Commands.Teach;

public class HarryPotterSpells extends JavaPlugin {
	
	@Override
	public void onEnable() {
		PM.log = getLogger();
		PM.hps = this;
		loadConfig();
		PlayerSpellConfig.getPSC();
		PlayerSpellConfig.savePSC();
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		if(getConfig().getBoolean("VaultEnabled") == true) {
			Vault.setupVault();
		}
		PM.log("Plugin enabled", Level.INFO);
	}
	
	@Override
	public void onDisable() {
		PM.log("Plugin disabled", Level.INFO);
	}
	
	public void loadConfig() {
		FileConfiguration c = getConfig();
		c.addDefault("TimeSpell.TimeToSet", 0);
		c.options().copyDefaults(true);
		saveConfig();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(commandLabel.equalsIgnoreCase("teach")) {
			if(sender instanceof Player) {
				Teach.teach((Player)sender, args);
			} else {
				Teach.teachConsole(args);
			}
			return true;
		}
		
		
		return true;
	}

}
