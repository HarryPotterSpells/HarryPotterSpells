package com.lavacraftserver.HarryPotterSpells;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.lavacraftserver.HarryPotterSpells.Commands.Sort;
import com.lavacraftserver.HarryPotterSpells.Commands.Teach;
import com.lavacraftserver.HarryPotterSpells.Commands.UnTeach;
import com.lavacraftserver.HarryPotterSpells.Hooks.LogBlock;
import com.lavacraftserver.HarryPotterSpells.Hooks.Vault;
import com.lavacraftserver.HarryPotterSpells.SpellLoading.SpellLoader;
import com.lavacraftserver.HarryPotterSpells.Spells.SpellManager;
import com.lavacraftserver.HarryPotterSpells.Utils.MiscListeners;

public class HarryPotterSpells extends JavaPlugin {
	public PlayerSpellConfig PlayerSpellConfig=new PlayerSpellConfig(this);
	public PM PM=new PM(this);
	public SpellManager spellManager=new SpellManager(this);
	public MiscListeners MiscListeners = new MiscListeners(this);
	public Listeners Listeners = new Listeners(this);
	public Vault Vault = new Vault(this);
	public LogBlock LogBlock=new LogBlock(this);
	public Logger log = Logger.getLogger("Minecraft");
	public Sort Sort = new Sort(this);
	public Teach Teach = new Teach(this);
	public SpellLoader loader;
	public UnTeach UnTeach = new UnTeach(this);
	
	@Override
	public void onEnable() {
		// General
		PM.clearStorage();
		
		// Config
		loadConfig();
		PlayerSpellConfig.getPSC();
		
		// Listeners
		getServer().getPluginManager().registerEvents(Listeners, this);
		getServer().getPluginManager().registerEvents(MiscListeners, this);
		
		// Hooks
		Vault.setupVault();
		LogBlock.setupLogBlock();
		
		// Misc Initialisation
		loader = new SpellLoader(this);
		
		// Plugin Metrics
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    // Sad face...
		}
		
		PM.log("Plugin enabled", Level.INFO);
	}
	
	@Override
	public void onDisable() {
		PM.clearStorage();
		PM.log("Plugin disabled", Level.INFO);
	}
	
	public void loadConfig() {
		File file = new File(this.getDataFolder(), "config.yml");
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
		if(commandLabel.equalsIgnoreCase("sort") && getConfig().getBoolean("SortingHat.enabled")) {
			if(sender instanceof Player) {
				Sort.go((Player)sender);
			} else {
				PM.log("You must be a player to be sorted.", Level.INFO);
			}
			return true;
		}
		if(commandLabel.equalsIgnoreCase("unteach")) {
			if(sender instanceof Player) {
				UnTeach.unTeach((Player)sender, args);
			} else {
				UnTeach.unTeachConsole(args);
			}
			return true;
		}
		
		return true;
	}

}
