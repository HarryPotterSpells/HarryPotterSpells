package com.lavacraftserver.HarryPotterSpells;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.lavacraftserver.HarryPotterSpells.Commands.Teach;
import com.lavacraftserver.HarryPotterSpells.Utils.MiscListeners;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

public class HarryPotterSpells extends JavaPlugin {
	
	@Override
	public void onEnable() {
		PM.log = getLogger();
		PM.hps = this;
		Listeners.currentSpell.clear();
		loadConfig();
		PlayerSpellConfig.getPSC();
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		getServer().getPluginManager().registerEvents(new Targeter(), this);
		getServer().getPluginManager().registerEvents(new MiscListeners(), this);
		if(getConfig().getBoolean("VaultEnabled") == true) {
			Vault.setupVault();
		}
		PM.log("Plugin enabled", Level.INFO);
	}
	
	@Override
	public void onDisable() {
		Listeners.currentSpell.clear();
		PM.log("Plugin disabled", Level.INFO);
	}
	
	public void loadConfig() {
		File file = new File(PM.hps.getDataFolder(), "config.yml");
		if(!file.exists()) {
			getConfig().options().copyDefaults(true);
			saveConfig();
		}
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
