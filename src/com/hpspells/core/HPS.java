package com.hpspells.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import com.hpspells.core.Metrics.Graph;
import com.hpspells.core.api.event.SpellBookRecipeAddEvent;
import com.hpspells.core.command.CommandInfo;
import com.hpspells.core.command.HCommandExecutor;
import com.hpspells.core.configuration.ConfigurationManager;
import com.hpspells.core.data.DataManager;
import com.hpspells.core.spell.Spell;
import com.hpspells.core.spell.SpellManager;
import com.hpspells.core.spell.interfaces.Craftable;
import com.hpspells.core.util.MetricStatistics;
import com.hpspells.core.util.ReflectionsReplacement;
import com.hpspells.core.util.SVPBypass;

public class HPS extends JavaPlugin {
	public ConfigurationManager ConfigurationManager;
	public PM PM;
	public SpellManager SpellManager;
	public Wand Wand;
	public SpellTargeter SpellTargeter;
	public Localisation Localisation;
	public DataManager DataManager;
	public net. VaultPermissions;

	private static CommandMap commandMap;
	private static Collection<HelpTopic> helpTopics = new ArrayList<HelpTopic>();

	@Override
	public void onEnable() {
	    // Instance loading 
	    PM = new PM(this);
	    Localisation = new Localisation(this);
		ConfigurationManager = new ConfigurationManager(this);
		SpellTargeter = new SpellTargeter(this);
		SpellManager = new SpellManager(this);
		DataManager = new DataManager(this);
		Wand = new Wand(this);

		// Configuration
		loadConfig();

		// Listeners
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
		getServer().getPluginManager().registerEvents(new MetricStatistics(), this);

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

		// Reflections - Commands
		int commands = 0;
		try {
            for(Class<? extends HCommandExecutor> clazz : ReflectionsReplacement.getSubtypesOf(HCommandExecutor.class, "com.hpspells.core.command", getClassLoader(), HCommandExecutor.class)) {
                if(addHackyCommand(clazz))
            		commands++;
            }
        } catch (Exception e) {
            PM.log(Level.WARNING, Localisation.getTranslation("errReflectionsReplacementCmd"));
            PM.debug(e);
        }
		PM.debug(Localisation.getTranslation("dbgRegisteredCoreCommands", commands));

	    Bukkit.getHelpMap().addTopic(new IndexHelpTopic("HarryPotterSpells", Localisation.getTranslation("hlpDescription"), "", helpTopics));
	    PM.debug(Localisation.getTranslation("dbgHelpCommandsAdded"));

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
        PM.debug(Localisation.getTranslation("dbgCraftingStart"));

		if(getConfig().getBoolean("wand.crafting.enabled", true)) {
		    try {
    		    ShapedRecipe wandRecipe = new ShapedRecipe(Wand.getLorelessWand());
    		    List<String> list = getConfig().getStringList("wand.crafting.recipe");
    		    Set<String> ingredients = getConfig().getConfigurationSection("wand.crafting.ingredients").getKeys(false);

    		    wandRecipe.shape(list.get(0), list.get(1), list.get(2));
    		    for(String string : ingredients)
    		        wandRecipe.setIngredient(string.toCharArray()[0], Material.getMaterial(getConfig().getInt("wand.crafting.ingredients." + string)));

    		    getServer().addRecipe(wandRecipe);
		    } catch(Exception e) { // It's surrounded by a try/catch block because we can't let any stupid errors in config disable the plugin.
		        PM.log(Level.INFO, Localisation.getTranslation("errCraftingChanges"));
		        PM.debug(e);
		    }
		}

		if(getConfig().getBoolean("spells-craftable", true)) {
		    for(Spell s : SpellManager.getSpells()) {
		        if(s instanceof Craftable) {
		            SpellBookRecipeAddEvent e = new SpellBookRecipeAddEvent(((Craftable) s).getCraftingRecipe());
		            getServer().getPluginManager().callEvent(e);
		            if(!e.isCancelled())
		                getServer().addRecipe(e.getRecipe());
		        }
		    }
		}

        PM.debug(Localisation.getTranslation("dbgCraftingEnd"));

		PM.log(Level.INFO, Localisation.getTranslation("genPluginEnabled"));
	}

	@Override
	public void onDisable() {

		PM.log(Level.INFO, Localisation.getTranslation("genPluginDisabled"));
	}

	public void loadConfig() {
		File file = new File(this.getDataFolder(), "config.yml");
		if(!file.exists()) {
			getConfig().options().copyDefaults(true);
			saveDefaultConfig();
		}
	}

    public ClassLoader getHPSClassLoader() {
        return getClassLoader();
    }

	/**
	 * Registers a {@link HackyCommand} to the plugin
	 * @param clazz a class that extends {@code CommandExecutor}
	 * @return {@code true} if the command was added successfully
	 */
	public boolean addHackyCommand(Class<? extends HCommandExecutor> clazz) {
		if(!clazz.isAnnotationPresent(CommandInfo.class)) {
			PM.log(Level.INFO, Localisation.getTranslation("errAddCommandMapAnnotation", clazz.getSimpleName()));
			return false;
		}

		CommandInfo cmdInfo = clazz.getAnnotation(CommandInfo.class);
		String name = cmdInfo.name().equals("") ? clazz.getSimpleName().toLowerCase() : cmdInfo.name();
		String permission = cmdInfo.permission().equals("") ? "HarryPotterSpells." + name : cmdInfo.permission();
		List<String> aliases = cmdInfo.aliases().equals("") ? new ArrayList<String>() : Arrays.asList(cmdInfo.aliases().split(","));
		Permission perm = new Permission(permission, PermissionDefault.getByName(cmdInfo.permissionDefault()));
		Bukkit.getServer().getPluginManager().addPermission(perm);
		HackyCommand hacky = new HackyCommand(this, name, Localisation.getTranslation(cmdInfo.description()), cmdInfo.usage(), aliases);
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
	private static class HackyCommand extends Command {
		private CommandExecutor executor;
		private HPS HPS;

		public HackyCommand(HPS instance, String name, String description, String usageMessage, List<String> aliases) {
			super(name, description, usageMessage, aliases);
			this.HPS = instance;
		}

		@Override
		public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		    String s = sender instanceof Player ? "/" : "";
		    if(executor == null)
		        sender.sendMessage(HPS.Localisation.getTranslation("cmdUnknown", s));
		    else if(!sender.hasPermission(getPermission()))
		        HPS.PM.dependantMessagingWarn(sender, getPermissionMessage());
		    else {
		        boolean success = executor.onCommand(sender, this, commandLabel, args);
		        if(!success)
		            HPS.PM.dependantMessagingTell(sender, ChatColor.RED + HPS.Localisation.getTranslation("cmdUsage", s, getUsage().replace("<command>", commandLabel)));
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
