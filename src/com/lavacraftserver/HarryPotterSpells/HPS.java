package com.lavacraftserver.HarryPotterSpells;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_5_R3.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Recipe;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import com.lavacraftserver.HarryPotterSpells.Commands.HCommand;
import com.lavacraftserver.HarryPotterSpells.Extensions.ExtensionManager;
import com.lavacraftserver.HarryPotterSpells.Jobs.ClearJob;
import com.lavacraftserver.HarryPotterSpells.Jobs.DisableJob;
import com.lavacraftserver.HarryPotterSpells.Jobs.EnableJob;
import com.lavacraftserver.HarryPotterSpells.Jobs.JobManager;
import com.lavacraftserver.HarryPotterSpells.SpellLoading.SpellLoader;
import com.lavacraftserver.HarryPotterSpells.Spells.SpellManager;
import com.lavacraftserver.HarryPotterSpells.Utils.Wand;

public class HPS extends JavaPlugin {
	public static PlayerSpellConfig PlayerSpellConfig;
	public static PM PM;
	public static SpellManager SpellManager;
	public static Wand Wand;
	public static SpellLoader SpellLoader;
	public static JavaPlugin Plugin;
	public static JobManager JobManager;
	public static ExtensionManager AddonManager;
	
	private static CommandMap commandMap;
	
	@Override
	public void onEnable() {
		
		// After (not anymore!) instance loading
		Plugin = this;
		
		//Before instance loading
		PlayerSpellConfig = new PlayerSpellConfig();
		PM = new PM();
		SpellManager = new SpellManager();
		Wand = new Wand();
		SpellLoader = new SpellLoader();
		JobManager = new JobManager();
		AddonManager = new ExtensionManager();
		
		// Hacky command map stuff
		try {
            Field f = CraftServer.class.getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (CommandMap) f.get(getServer());
        } catch (Throwable e){
            PM.log(Level.SEVERE, "Could not access the command map. Commands will not work. Are you sure the plugin is up to date?");
            PM.debug(e);
        }
		
		Reflections reflections = new Reflections("com.lavacraftserver.HarryPotterSpells");
		
		// Reflections - Jobs
		int clearJobs = 0;
		for(Class<? extends ClearJob> c : reflections.getSubTypesOf(ClearJob.class)) {
			try {
				JobManager.addClearJob(c.newInstance());
				clearJobs++;
			} catch (InstantiationException | IllegalAccessException e) {
				PM.log(Level.WARNING, "An error occurred whilst the clear job in class " + c.getSimpleName() + " to the Job Manager.");
				PM.debug(e);
			}
		}
		
		int enableJobs = 0;
		for(Class<? extends EnableJob> c : reflections.getSubTypesOf(EnableJob.class)) {
			try {
				JobManager.addEnableJob(c.newInstance());
				enableJobs++;
			} catch(InstantiationException | IllegalAccessException e) {
				PM.log(Level.WARNING, "An error occurred whilst adding the enable job in class " + c.getSimpleName() + " to the Job Manager.");
				PM.debug(e);
			}
		}

		int disableJobs = 0;
		for(Class<? extends DisableJob> c : reflections.getSubTypesOf(DisableJob.class)) {
			try {
				JobManager.addDisableJob(c.newInstance());
				disableJobs++;
			} catch (InstantiationException | IllegalAccessException e) {
				PM.log(Level.WARNING, "An error occurred whilst adding the disable job in class " + c.getSimpleName() + " to the Job Manager");
				PM.debug(e);
			}
		}
		
		PM.log(Level.INFO, "Registered " + clearJobs + " core clear jobs, " + enableJobs + " core enable jobs and " + disableJobs + " core disable jobs.");
		
		// Reflections - Commands
		int commands = 0;
		for(Class<? extends CommandExecutor> clazz : reflections.getSubTypesOf(CommandExecutor.class)) {
			if(addHackyCommand(clazz))
				commands++;
		}
		PM.log(Level.INFO, "Registered " + commands + " core commands.");
		
		// Reflections - Listeners
		int listeners = 0;
		for(Class<? extends Listener> clazz : reflections.getSubTypesOf(Listener.class)) {
			try {
				getServer().getPluginManager().registerEvents(clazz.newInstance(), this);
			} catch (InstantiationException | IllegalAccessException e) {
				PM.log(Level.WARNING, "An error occurred whilst registering the listener in class " + clazz.getSimpleName() + ".");
				PM.debug(e);
			}
		}
		PM.log(Level.INFO, "Registered " + listeners + " core listeners.");
		
		// Plugin Metrics
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
		    PM.log(Level.WARNING, "An error occurred whilst enabling Plugin Metrics.");
		    PM.debug(e);
		}
		
		// Crafting Changes
		PM.log(Level.INFO, "Implementing crafting changes...");
		boolean disableAll = getConfig().getBoolean("disable-all-crafting", false), disableWand = getConfig().getBoolean("disable-wand-crafting", true);
		int wand = getConfig().getInt("wand-id", 280);
		
		if(disableAll) {
			getServer().clearRecipes();
			PM.debug("Removed all crafting recipes.");
			PM.log(Level.INFO, "Crafting changes implemented.");
			return;
		} else if(disableWand) {
			Iterator<Recipe> it = getServer().recipeIterator();
			while(it.hasNext()) {
				Recipe current = it.next();
				if(current.getResult().getTypeId() == wand) {
					PM.debug("Removed recipe for " + current.getResult().toString() + ".");
					it.remove();
				}
			}
		}
		PM.log(Level.INFO, "Crafting changes implemented.");
		
		JobManager.executeEnableJobs(getServer().getPluginManager());
		
		PM.log(Level.INFO, "Plugin enabled");
	}
	
	@Override
	public void onDisable() {
		JobManager.executeClearJobs();
		JobManager.executeDisableJob(getServer().getPluginManager());
		SpellManager.save();
		
		PM.log(Level.INFO, "Plugin disabled");
	}
	
	public void loadConfig() {
		File file = new File(this.getDataFolder(), "config.yml");
		if(!file.exists()) {
			getConfig().options().copyDefaults(true);
			saveConfig();
		}
	}
	
	/**
	 * Registers a hacky command to the plugin
	 * @param clazz a class that extends {@code CommandExecutor}
	 * @return {@code true} if the command was added successfully
	 */
	public static boolean addHackyCommand(Class<? extends CommandExecutor> clazz) {
		if(!clazz.isAnnotationPresent(HCommand.class)) {
			PM.log(Level.INFO, "Could not add command " + clazz.getSimpleName().toLowerCase() + " to the command map. It is missing the @HCommand annotation.");
			return false;
		}
		
		HCommand cmdInfo = clazz.getAnnotation(HCommand.class);
		String name = cmdInfo.name().equals("") ? clazz.getSimpleName().toLowerCase() : cmdInfo.name();
		String permission = cmdInfo.permission().equals("") ? "HarryPotterSpells." + name : cmdInfo.permission();
		List<String> aliases = cmdInfo.aliases().equals("") ? null : Arrays.asList(cmdInfo.aliases().split(","));
		
		Bukkit.getServer().getPluginManager().addPermission(new Permission(cmdInfo.aliases().equals("") ? "HarryPotterSpells." + clazz.getSimpleName() : cmdInfo.permission(), PermissionDefault.getByName(cmdInfo.permissionDefault())));
		HackyCommand hacky = new HackyCommand(name, cmdInfo.description(), cmdInfo.usage(), aliases);
		try {
			hacky.setExecutor(clazz.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			PM.log(Level.WARNING, "Could not add command " + name + " to the command map.");
			if(Plugin.getConfig().getBoolean("DebugMode", false))
				e.printStackTrace();
			return false;
		}
		commandMap.register("", hacky);
		Plugin.getCommand(name).setPermission(permission);
		return true;
	}
	
	/**
	 * A very hacky class used to register commands at plugin runtime
	 */
	private static class HackyCommand extends Command {
		private CommandExecutor executor;

		public HackyCommand(String name, String description, String usageMessage, List<String> aliases) {
			super(name, description, usageMessage, aliases);
		}

		@Override
		public boolean execute(CommandSender sender, String commandLabel, String[] args) {
			return executor != null ? executor.onCommand(sender, this, commandLabel, args) : false;
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
