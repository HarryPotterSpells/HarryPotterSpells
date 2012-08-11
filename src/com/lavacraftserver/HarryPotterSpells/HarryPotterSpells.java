package com.lavacraftserver.HarryPotterSpells;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.lavacraftserver.HarryPotterSpells.Commands.Sort;
import com.lavacraftserver.HarryPotterSpells.Commands.Teach;
import com.lavacraftserver.HarryPotterSpells.Hooks.LogBlock;
import com.lavacraftserver.HarryPotterSpells.Hooks.Vault;
import com.lavacraftserver.HarryPotterSpells.Spells.SpellManager;
import com.lavacraftserver.HarryPotterSpells.Utils.MiscListeners;
import com.lavacraftserver.HarryPotterSpells.api.SpellLoader;

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
	@Override
	public void onEnable() {
		Listeners.currentSpell.clear();
		
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
		
		PM.log("Plugin enabled", Level.INFO);
	}
	
	@Override
	public void onDisable() {
		Listeners.currentSpell.clear();
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
		
		
		return true;
	}

}
