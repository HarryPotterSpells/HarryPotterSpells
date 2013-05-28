package com.lavacraftserver.HarryPotterSpells;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.reflections.Reflections;

import com.lavacraftserver.HarryPotterSpells.Jobs.ClearJob;
import com.lavacraftserver.HarryPotterSpells.Jobs.JobManager;
import com.lavacraftserver.HarryPotterSpells.SpellLoading.SpellLoader;
import com.lavacraftserver.HarryPotterSpells.Spells.SpellManager;
import com.lavacraftserver.HarryPotterSpells.Utils.CommandDispatcher;
import com.lavacraftserver.HarryPotterSpells.Utils.MiscListeners;
import com.lavacraftserver.HarryPotterSpells.Utils.Wand;

public class HarryPotterSpells extends JavaPlugin {
	public static PlayerSpellConfig PlayerSpellConfig;
	public static PM PM;
	public static SpellManager SpellManager;
	public static MiscListeners MiscListeners;
	public static Listeners Listeners;
	public static Wand Wand;
	public static SpellLoader SpellLoader;
	public static CommandDispatcher CommandDispatcher;
	public static PluginManager PluginManager = Bukkit.getServer().getPluginManager();
	public static Plugin Plugin;
	public static JobManager JobManager;
	
	@Override
	public void onEnable() {
		//Before instance loading
		PlayerSpellConfig = new PlayerSpellConfig();
		PM = new PM();
		SpellManager = new SpellManager();
		MiscListeners = new MiscListeners();
		Listeners = new Listeners();
		Wand = new Wand();
		SpellLoader = new SpellLoader();
		CommandDispatcher = new CommandDispatcher();
		JobManager = new JobManager();
		
		// Reflections
		Reflections reflections = new Reflections("com.lavacraftserver.HarryPotterSpells");
		
		for(Class<? extends ClearJob> c : reflections.getSubTypesOf(ClearJob.class)) {
			try {
				JobManager.addClearJob(c.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				PM.log("An error occurred whilst adding a ClearJob to the JobManager. Please report this error.", Level.WARNING);
				e.printStackTrace();
			}
		}
		
		// Config
		loadConfig();
		PlayerSpellConfig.getPSC();
		
		// Listeners
		getServer().getPluginManager().registerEvents(Listeners, this);
		getServer().getPluginManager().registerEvents(MiscListeners, this); //TODO automated addition
		
		// Plugin Metrics
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    PM.log("An error occurred whilst enabling Plugin Metrics. " + e.getMessage() + ".", Level.WARNING);
		}
		
		// Crafting Changes
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
		
		// After instance loading
		Plugin = this;
		
		PM.log("Plugin enabled", Level.INFO);
	}
	
	@Override
	public void onDisable() {
		JobManager.executeClearJobs();
		SpellManager.save();
		
		PM.log("Plugin disabled", Level.INFO);
	}
	
	public void loadConfig() {
		File file = new File(this.getDataFolder(), "config.yml");
		if(!file.exists()) {
			getConfig().options().copyDefaults(true);
			saveConfig();
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return CommandDispatcher.onCommand(sender, cmd, commandLabel, args);
	}

}
