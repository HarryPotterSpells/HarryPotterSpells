package com.lavacraftserver.HarryPotterSpells;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.inventory.Recipe;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import com.lavacraftserver.HarryPotterSpells.Metrics.Graph;
import com.lavacraftserver.HarryPotterSpells.Commands.HCommand;
import com.lavacraftserver.HarryPotterSpells.Commands.HCommandExecutor;
import com.lavacraftserver.HarryPotterSpells.Extensions.ExtensionManager;
import com.lavacraftserver.HarryPotterSpells.Jobs.ClearJob;
import com.lavacraftserver.HarryPotterSpells.Jobs.DisableJob;
import com.lavacraftserver.HarryPotterSpells.Jobs.EnableJob;
import com.lavacraftserver.HarryPotterSpells.Jobs.JobManager;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;
import com.lavacraftserver.HarryPotterSpells.Spells.SpellManager;
import com.lavacraftserver.HarryPotterSpells.Utils.MetricStatistics;
import com.lavacraftserver.HarryPotterSpells.Utils.SVPBypass;

public class HPS extends JavaPlugin {
	public PlayerSpellConfig PlayerSpellConfig;
	public PM PM;
	public SpellManager SpellManager;
	public Wand Wand;
	public JobManager JobManager;
	public ExtensionManager ExtensionManager;
	public SpellTargeter SpellTargeter;
	public Localisation Localisation;
	
	private CommandMap commandMap;
	private Collection<HelpTopic> helpTopics = new ArrayList<HelpTopic>();
	
	@Override
	public void onEnable() {
	    // Instance loading 
	    // ORDER IS IMPORTANT.
	    PM = new PM(this);
	    Localisation = new Localisation(this);
		PlayerSpellConfig = new PlayerSpellConfig(this);
		SpellManager = new SpellManager(this);
		Wand = new Wand(this);
		JobManager = new JobManager();
		ExtensionManager = new ExtensionManager(this);
		
		// Configuration
		loadConfig();
		
		// Hacky command map stuff
		try {
		    Class<?> craftServer = SVPBypass.getCurrentCBClass("CraftServer");
		    if(craftServer == null)
		        throw new Throwable("Computer says no");
            Field f = craftServer.getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (CommandMap) f.get(getServer());
        } catch (Throwable e){
            PM.log(Level.SEVERE, Localisation.getTranslation("errCommandMap"));
            PM.debug(e);
        }
		
		Reflections reflections = new Reflections("com.lavacraftserver.HarryPotterSpells");
		
		// Reflections - Jobs
		int clearJobs = 0;
		for(Class<? extends ClearJob> c : reflections.getSubTypesOf(ClearJob.class)) {
			try {
				JobManager.addClearJob(c.getConstructor(HPS.class).newInstance(this));
				clearJobs++;
			} catch (Exception e) {
				PM.log(Level.WARNING, Localisation.getTranslation("errClearJob", c.getSimpleName()));
				PM.debug(e);
			}
		}
		
		int enableJobs = 0;
		for(Class<? extends EnableJob> c : reflections.getSubTypesOf(EnableJob.class)) {
			try {
				JobManager.addEnableJob(c.getConstructor(HPS.class).newInstance(this));
				enableJobs++;
			} catch(Exception e) {
                PM.log(Level.WARNING, Localisation.getTranslation("errEnableJob", c.getSimpleName()));
				PM.debug(e);
			}
		}

		int disableJobs = 0;
		for(Class<? extends DisableJob> c : reflections.getSubTypesOf(DisableJob.class)) {
			try {
				JobManager.addDisableJob(c.getConstructor(HPS.class).newInstance(this));
				disableJobs++;
			} catch (Exception e) {
                PM.log(Level.WARNING, Localisation.getTranslation("errDisableJob", c.getSimpleName()));
				PM.debug(e);
			}
		}
		
		PM.debug(Localisation.getTranslation("dbgRegisteredCoreJobs", clearJobs, enableJobs, disableJobs));
		
		// Reflections - Commands
		int commands = 0;
		for(Class<? extends HCommandExecutor> clazz : reflections.getSubTypesOf(HCommandExecutor.class)) {
		    if(clazz.getName().equals(HCommandExecutor.class.getName()))
		        continue;
		    else if(addHackyCommand(clazz))
				commands++;
		}
		PM.debug(Localisation.getTranslation("dbgRegisteredCoreCommands", commands));
		
		Bukkit.getHelpMap().addTopic(new IndexHelpTopic("HarryPotterSpells", Localisation.getTranslation("hlpDescription"), "", helpTopics));
		
		PM.debug(Localisation.getTranslation("dbgHelpCommandsAdded"));
		
		// Reflections - Listeners
		int listeners = 0;
		for(Class<? extends Listener> clazz : reflections.getSubTypesOf(Listener.class)) {
			try {
				getServer().getPluginManager().registerEvents(clazz.getConstructor(HPS.class).newInstance(this), this);
				listeners++;
			} catch (Exception e) {
				PM.log(Level.WARNING, Localisation.getTranslation("errListeners", clazz.getSimpleName()));
				PM.debug(e);
			}
		}
		PM.debug(Localisation.getTranslation("dbgRegisteredCoreListeners", listeners));
		
		// Plugin Metrics
		try {
		    Metrics metrics = new Metrics(this);
		    
		    // Total amount of spells cast
		    metrics.addCustomData(new Metrics.Plotter("Total Amount of Spells Cast") {
                
                @Override
                public int getValue() {
                    return MetricStatistics.getSpellsCast();
                }
                
            });
		    
		    // Types of spell cast
		    Graph typesOfSpellCast = metrics.createGraph("Types of Spell Cast");
		    
		    for(final Spell spell : SpellManager.getSpells()) {
		        typesOfSpellCast.addPlotter(new Metrics.Plotter(spell.getName()) {
                    
                    @Override
                    public int getValue() {
                        return MetricStatistics.getAmountOfTimesCast(spell);
                    }
                    
                });
		    }
		    
		    // Spell success rate
		    Graph spellSuccessRate = metrics.createGraph("Spell Success Rate");
		    
		    spellSuccessRate.addPlotter(new Metrics.Plotter("Successes") {

                @Override
                public int getValue() {
                    return MetricStatistics.getSuccesses();
                }
		        
		    });
		    
		    spellSuccessRate.addPlotter(new Metrics.Plotter("Failures") {
                
                @Override
                public int getValue() {
                    return MetricStatistics.getFailures();
                }
            });
		    
		    metrics.start();
		} catch (IOException e) {
		    PM.log(Level.WARNING, Localisation.getTranslation("errPluginMetrics"));
		    PM.debug(e);
		}
		
		// Crafting Changes
		PM.debug(Localisation.getTranslation("dbgCraftingChanges"));
		boolean disableAll = getConfig().getBoolean("disable-all-crafting", false), disableWand = getConfig().getBoolean("disable-wand-crafting", true);
		int wand = getConfig().getInt("wand-id", 280);
		
		if(disableAll) {
			getServer().clearRecipes();
			PM.debug(Localisation.getTranslation("dbgCraftingRemoved"));
		} else if(disableWand) {
			Iterator<Recipe> it = getServer().recipeIterator();
			while(it.hasNext()) {
				Recipe current = it.next();
				if(current.getResult().getTypeId() == wand) {
					PM.debug(Localisation.getTranslation("dbgRemovedRecipe", current.getResult().toString()));
					it.remove();
				}
			}
		}
		PM.debug(Localisation.getTranslation("dbgCraftingChangesDone"));
		
		JobManager.executeEnableJobs(getServer().getPluginManager());
		
		PM.log(Level.INFO, Localisation.getTranslation("genPluginEnabled"));
	}
	
	@Override
	public void onDisable() {
		JobManager.executeClearJobs();
		JobManager.executeDisableJob(getServer().getPluginManager());

		PM.log(Level.INFO, Localisation.getTranslation("genPluginDisabled"));
	}
	
	public void loadConfig() {
		File file = new File(this.getDataFolder(), "config.yml");
		if(!file.exists()) {
			getConfig().options().copyDefaults(true);
			saveDefaultConfig();
		}
	}
	
	/**
	 * Registers a {@link HackyCommand} to the plugin
	 * @param clazz a class that extends {@code CommandExecutor}
	 * @return {@code true} if the command was added successfully
	 */
	public boolean addHackyCommand(Class<? extends HCommandExecutor> clazz) {
		if(!clazz.isAnnotationPresent(HCommand.class)) {
			PM.log(Level.INFO, Localisation.getTranslation("errAddCommandMapAnnotation", clazz.getSimpleName()));
			return false;
		}
		
		HCommand cmdInfo = clazz.getAnnotation(HCommand.class);
		String name = cmdInfo.name().equals("") ? clazz.getSimpleName().toLowerCase() : cmdInfo.name();
		String permission = cmdInfo.permission().equals("") ? "HarryPotterSpells." + name : cmdInfo.permission();
		List<String> aliases = cmdInfo.aliases().equals("") ? new ArrayList<String>() : Arrays.asList(cmdInfo.aliases().split(","));
		Permission perm = new Permission(permission, PermissionDefault.getByName(cmdInfo.permissionDefault()));
		Bukkit.getServer().getPluginManager().addPermission(perm);
		HackyCommand hacky = new HackyCommand(name, Localisation.getTranslation(cmdInfo.description()), cmdInfo.usage(), aliases);
		hacky.setPermission(permission);
		hacky.setPermissionMessage(Localisation.getTranslation(cmdInfo.noPermissionMessage()));
		try {
			hacky.setExecutor(clazz.getConstructor(HPS.class).newInstance(this));
		} catch (Exception e) {
			PM.log(Level.WARNING, Localisation.getTranslation("errAddCommandMap", clazz.getSimpleName()));
			PM.debug(e);
			return false;
		}
		commandMap.register("", hacky);
		helpTopics.add(new GenericCommandHelpTopic(hacky));
		return true;
	}
	
	/**
	 * A very hacky class used to register commands at plugin runtime
	 */
	private class HackyCommand extends Command {
		private CommandExecutor executor;

		public HackyCommand(String name, String description, String usageMessage, List<String> aliases) {
			super(name, description, usageMessage, aliases);
		}

		@Override
		public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		    String s = sender instanceof Player ? "/" : "";
		    if(executor == null)
		        sender.sendMessage(Localisation.getTranslation("cmdUnknown", s));
		    else if(!sender.hasPermission(getPermission()))
		        PM.dependantMessagingWarn(sender, getPermissionMessage());
		    else {
		        boolean success = executor.onCommand(sender, this, commandLabel, args);
		        if(!success)
		            PM.dependantMessagingTell(sender, ChatColor.RED + Localisation.getTranslation("cmdUsage", s, getUsage().replace("<command>", commandLabel)));
		    }
		    return true;
		}
		
		/**
		 * Sets the executor to be used for this hacky command
		 * @param executor the executor
		 */
		public void setExecutor(CommandExecutor executor) {
			this.executor = executor;
		}
		
	}
	
}
