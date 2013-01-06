package com.lavacraftserver.HarryPotterSpells;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import com.lavacraftserver.HarryPotterSpells.Hooks.LogBlock;
import com.lavacraftserver.HarryPotterSpells.Hooks.Towny;
import com.lavacraftserver.HarryPotterSpells.Hooks.Vault;
import com.lavacraftserver.HarryPotterSpells.Hooks.WorldGuard;
import com.lavacraftserver.HarryPotterSpells.SpellLoading.SpellLoader;
import com.lavacraftserver.HarryPotterSpells.Spells.SpellManager;
import com.lavacraftserver.HarryPotterSpells.Utils.CommandDispatcher;
import com.lavacraftserver.HarryPotterSpells.Utils.MiscListeners;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;
import com.lavacraftserver.HarryPotterSpells.Utils.Wand;

public class HarryPotterSpells extends JavaPlugin {
	public PlayerSpellConfig PlayerSpellConfig=new PlayerSpellConfig(this);
	public PM PM=new PM(this);
	public SpellManager spellManager=new SpellManager(this);
	public MiscListeners MiscListeners = new MiscListeners(this);
	public Listeners Listeners = new Listeners(this);
	public Vault Vault = new Vault(this);
	public LogBlock LogBlock=new LogBlock(this);
	public WorldGuard WorldGuard = new WorldGuard(this);
	public Towny Towny = new Towny(this);
	public Wand Wand = new Wand(this);
	public Targeter Targeter = new Targeter(this);
	public Logger log = Logger.getLogger("Minecraft");
	public SpellLoader SpellLoader;
	public CommandDispatcher CommandDispatcher;
	
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
		WorldGuard.setupWorldGuard();
		Towny.setupTowny();
		
		// Misc Initialisation
		SpellLoader = new SpellLoader(this);
		CommandDispatcher = new CommandDispatcher(this);
		
		// Plugin Metrics
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    PM.log("An error occurred whilst enabling Plugin Metrics. " + e.getMessage() + ".", Level.WARNING);
		}
		
		// Enable Functions
		craftingChanges();
		
		PM.log("Plugin enabled", Level.INFO);
	}
	
	@Override
	public void onDisable() {
		PM.clearStorage();
		spellManager.save();
		
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
		return CommandDispatcher.onCommand(sender, cmd, commandLabel, args);
	}
	
	/*
	 * STORE ENABLE/DISABLE FUNCTIONS BELOW FOR READABILITY :)
	 */
	
	private void craftingChanges() {
		PM.log("Implementing crafting changes...", Level.INFO);
		boolean disableAll = getConfig().getBoolean("disable-all-crafting", false), disableWand = getConfig().getBoolean("disable-wand-crafting", true);
		int wand = getConfig().getInt("wand-id", 280);
		
		if(disableAll) {
			getServer().clearRecipes();
			PM.debug("Removed all crafting recipes.");
			PM.log("Crafting changes implemented.", Level.INFO);
			return;
		}
		
		Iterator<Recipe> it = getServer().recipeIterator();
		while(it.hasNext()) {
			Recipe current = it.next();
			if(disableWand && current.getResult().getTypeId() == wand) {
				PM.debug("Removed recipe for " + current.getResult().toString() + ".");
				it.remove();
			}
		}
		PM.log("Crafting changes implemented.", Level.INFO);
	}

}
