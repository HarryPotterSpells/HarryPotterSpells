package com.hpspells.core;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import com.hpspells.core.Localisation.Language;
import com.hpspells.core.api.APIHandler;
import com.hpspells.core.api.event.SpellBookRecipeAddEvent;
import com.hpspells.core.command.CommandInfo;
import com.hpspells.core.command.HCommandExecutor;
import com.hpspells.core.configuration.ConfigurationManager;
import com.hpspells.core.configuration.ConfigurationManager.ConfigurationType;
import com.hpspells.core.configuration.CustomConfiguration;
import com.hpspells.core.configuration.PlayerSpellConfig;
import com.hpspells.core.extension.ExtensionManager;
import com.hpspells.core.spell.Spell;
import com.hpspells.core.spell.SpellManager;
import com.hpspells.core.spell.interfaces.Craftable;
import com.hpspells.core.util.MetricStatistics;
import com.hpspells.core.util.ReflectionsReplacement;
import com.hpspells.core.util.SVPBypass;

public class HPS extends JavaPlugin {
    public static HPS instance;
    
    public ConfigurationManager ConfigurationManager;
    public PM PM;
    public SpellManager SpellManager;
    public WandManager WandManager;
    public SpellTargeter SpellTargeter;
    public Localisation Localisation;
    public ExtensionManager ExtensionManager;

    private static List<ItemStack> recipeResults = new ArrayList<ItemStack>();
    private static CommandMap commandMap;
    private static Collection<HelpTopic> helpTopics = new ArrayList<HelpTopic>();

    /**
     * State of the Localisation class, if it requires the plugin to disable 
     * {@code state = false;} otherwise state will always be true
     */
    public boolean localeState = true;
    
    @Override
    public void onEnable() {
        instance = this;
        // Instance loading
        PM = new PM(this);
        ConfigurationManager = new ConfigurationManager(this);
        Localisation = new Localisation(this);
        if (localeState) {
        	SpellTargeter = new SpellTargeter(this);
            SpellManager = new SpellManager(this);
            WandManager = new WandManager(this);
            ExtensionManager = new ExtensionManager(this);
            new APIHandler(this);
            
            // Configuration
            ConfigurationManager.loadConfig();

            PlayerSpellConfig PSC = (PlayerSpellConfig) ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);
            Double version = PSC.get().getDouble("VERSION_DO_NOT_EDIT", -1d) == -1d ? null : PSC.get().getDouble("VERSION_DO_NOT_EDIT", -1d);
            
            if (new File(getDataFolder(), "PlayerSpellConfig.yml").exists()) {
            	if (version == null || version < PlayerSpellConfig.CURRENT_VERSION) {
                    // STORE UPDATES HERE

                    if (version == null) { // Updating from unformatted version to version 1.1
                        PM.log(Level.INFO, Localisation.getTranslation("pscOutOfDate"));

                        Map<String, List<String>> newConfig = new HashMap<String, List<String>>(), lists = new HashMap<String, List<String>>();
                        Set<String> css = new HashSet<String>();

                        for (String s : PSC.get().getKeys(false)) // This seemingly stupid code is to avoid a ConcurrencyModificationException (google it)
                            css.add(s);

                        for (String s : css)
                            lists.put(s, PSC.get().getStringList(s));

                        for (String cs : css) {
                            List<String> list = new ArrayList<String>();
                            for (String spellString : PSC.get().getStringList(cs)) {
                                if (spellString.equals("AlarteAscendare")) {
                                    list.add("Alarte Ascendare");
                                } else if (spellString.equals("AvadaKedavra")) {
                                    list.add("Avada Kedavra");
                                } else if (spellString.equals("FiniteIncantatem")) {
                                    list.add("Finite Incantatem");
                                } else if (spellString.equals("MagnaTonitrus")) {
                                    list.add("Magna Tonitrus");
                                } else if (spellString.equals("PetrificusTotalus")) {
                                    list.add("Petrificus Totalus");
                                } else if (spellString.equals("TimeSpell")) {
                                    list.add("Time");
                                } else if (spellString.equals("TreeSpell")) {
                                    list.add("Tree");
                                } else if (spellString.equals("WingardiumLeviosa")) {
                                    list.add("Wingardium Leviosa");
                                } else {
                                    list.add(spellString);
                                }
                            }

                            newConfig.put(cs, list);
                        }

                        for (Entry<String, List<String>> ent : newConfig.entrySet())
                            PSC.get().set(ent.getKey(), ent.getValue());

                        PSC.get().set("VERSION_DO_NOT_EDIT", 1.1d);
                        PSC.save();

                        PM.log(Level.INFO, Localisation.getTranslation("pscUpdated", "1.1"));
                    }
                }
            }

            // Listeners
            getServer().getPluginManager().registerEvents(new Listeners(this), this);
            getServer().getPluginManager().registerEvents(new MetricStatistics(), this);

            // Hacky command map stuff
            try {
                Class<?> craftServer = SVPBypass.getCurrentCBClass("CraftServer");
                if (craftServer == null)
                    throw new Throwable("Computer says no");
                Field f = craftServer.getDeclaredField("commandMap");
                f.setAccessible(true);
                commandMap = (CommandMap) f.get(getServer());
            } catch (Throwable e) {
                PM.log(Level.SEVERE, Localisation.getTranslation("errCommandMap"));
                PM.debug(e);
            }

            // Reflections - Commands
            int commands = 0;
            try {
                for (Class<? extends HCommandExecutor> clazz : ReflectionsReplacement.getSubtypesOf(HCommandExecutor.class, "com.hpspells.core.command", getClassLoader(), HCommandExecutor.class)) {
                    if (addHackyCommand(clazz))
                        commands++;
                }
            } catch (Exception e) {
                PM.log(Level.WARNING, Localisation.getTranslation("errReflectionsReplacementCmd"));
                PM.debug(e);
            }
            
//            HPSTabCompleter completer = new HPSTabCompleter();
//            getCommand("spellinfo").setTabCompleter(completer);
//            getCommand("spellswitch").setTabCompleter(completer);
//            getCommand("teach").setTabCompleter(completer);
//            getCommand("unteach").setTabCompleter(completer);
            
            PM.debug(Localisation.getTranslation("dbgRegisteredCoreCommands", commands));

            Bukkit.getHelpMap().addTopic(new IndexHelpTopic("HarryPotterSpells", Localisation.getTranslation("hlpDescription"), "", helpTopics));
            PM.debug(Localisation.getTranslation("dbgHelpCommandsAdded"));

            // Plugin Metrics
            setupCharts(new Metrics(this));

            // Crafting Changes
            PM.debug(Localisation.getTranslation("dbgCraftingStart"));
            setupCrafting();
            PM.debug(Localisation.getTranslation("dbgCraftingEnd"));

            // Extension manager setup
            PM.debug(Localisation.getTranslation("dbgExtensionStart"));
            ExtensionManager.loadExtensions();
            ExtensionManager.enableExtensions();
            PM.debug(Localisation.getTranslation("dbgExtensionStop"));

            PM.log(Level.INFO, Localisation.getTranslation("genPluginEnabled"));
        }
    }

    @Override
    public void onDisable() {
        ExtensionManager.disableExtensions();
    	if (localeState)
    		PM.log(Level.INFO, Localisation.getTranslation("genPluginDisabled"));
    	else
    		PM.log(Level.INFO, "Plugin disabled.");
    }
    
    public boolean onReload() {
        reloadConfig();
        ConfigurationManager.reloadConfigAll();
        Localisation.load();
        ExtensionManager.reloadExtensions();
        this.WandManager = new WandManager(this);
        if (!setupCrafting()) return false;
        return true;
    }
    
    private void setupCharts(Metrics metrics) {
    	try {
    		// Total Amount of Spells Casted
//            metrics.addCustomChart(new Metrics.MultiLineChart("spells_casted_total", () -> {
//            	Map<String, Integer> spellsCastMap = new HashMap<>();
//				spellsCastMap.put("Total", MetricStatistics.getSpellsCast());
//				spellsCastMap.put("Hits", MetricStatistics.getSuccesses());
//				spellsCastMap.put("Misses", MetricStatistics.getFailures());
//				return spellsCastMap;
//            }));
    		
    		metrics.addCustomChart(new Metrics.SingleLineChart("total_spells_casted", () -> MetricStatistics.getSpellsCast()));
    		metrics.addCustomChart(new Metrics.SingleLineChart("total_spells_successes", () -> MetricStatistics.getSuccesses()));
    		metrics.addCustomChart(new Metrics.SingleLineChart("total_spells_failures", () -> MetricStatistics.getFailures()));
    		
            // Amount of Spells Casted per Spell
            metrics.addCustomChart(new Metrics.SimpleBarChart("spells_casted_single", () -> {
            	Map<String, Integer> spellsCastMap = new HashMap<>();
				for (Spell spell : SpellManager.getSpells()) {
					spellsCastMap.put(spell.getName(), MetricStatistics.getAmountOfTimesCast(spell));
				}
				return spellsCastMap;
            } ));
            
            // Language used
            metrics.addCustomChart(new Metrics.SimplePie("Language", () -> Language.getLanuage(getConfig().getString("language")).toString()));
        } catch (Exception e) {
            PM.log(Level.WARNING, Localisation.getTranslation("errPluginMetrics"));
            PM.debug(e);
        }
    }
    
    public boolean setupCrafting() {
        if (!recipeResults.isEmpty()) {
        	recipeResults.clear();
            getServer().resetRecipes();
        }
    	
        if (getConfig().getBoolean("wand.crafting.enabled", true)) {
            try {
                ShapedRecipe wandRecipe = new ShapedRecipe(new NamespacedKey(this, "wand"), WandManager.getLorelessWand());
                List<String> list = getConfig().getStringList("wand.crafting.recipe");
                Set<String> ingredients = getConfig().getConfigurationSection("wand.crafting.ingredients").getKeys(false);

                wandRecipe.shape(list.get(0), list.get(1), list.get(2));
                for (String string : ingredients) {
                    wandRecipe.setIngredient(string.toCharArray()[0], Material.matchMaterial(getConfig().getString("wand.crafting.ingredients." + string)));
                }

                recipeResults.add(wandRecipe.getResult());
                getServer().addRecipe(wandRecipe);
                return true;
            } catch (NullPointerException e) { // It's surrounded by a try/catch block because we can't let any stupid errors in config disable the plugin.
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.isOp()) {
                        PM.tell(player, ChatColor.RED + "Unable to load material from crafting config");
                        PM.tell(player, "Please check that all values are correct");
                    }
                }
                PM.log(Level.INFO, Localisation.getTranslation("errCraftingChanges"));
                PM.debug(e);
                return false;
            } catch (Exception e) {
                PM.log(Level.INFO, Localisation.getTranslation("errCraftingChanges"));
                PM.debug(e);
                return false;
            }
        }

        if (getConfig().getBoolean("spells-craftable", true)) {
            for (Spell s : SpellManager.getSpells()) {
                if (s instanceof Craftable) {
                    SpellBookRecipeAddEvent e = new SpellBookRecipeAddEvent(((Craftable) s).getCraftingRecipe());
                    getServer().getPluginManager().callEvent(e);
                    if (!e.isCancelled()) {
                        recipeResults.add(e.getRecipe().getResult());
                        getServer().addRecipe(e.getRecipe());
                    }
                }
            }
        }
        return true;
    }

    public ClassLoader getHPSClassLoader() {
        return getClassLoader();
    }
    
    public CustomConfiguration getConfig(ConfigurationType type) {
    	return ConfigurationManager.getConfig(type);
    }

    /**
     * Registers a {@link HackyCommand} to the plugin
     *
     * @param clazz a class that extends {@code CommandExecutor}
     * @return {@code true} if the command was added successfully
     */
    public boolean addHackyCommand(Class<? extends HCommandExecutor> clazz) {
        if (!clazz.isAnnotationPresent(CommandInfo.class)) {
            PM.log(Level.INFO, Localisation.getTranslation("errAddCommandMapAnnotation", clazz.getSimpleName()));
            return false;
        }

        CommandInfo cmdInfo = clazz.getAnnotation(CommandInfo.class);
        String name = cmdInfo.name().equals("") ? clazz.getSimpleName().toLowerCase() : cmdInfo.name();
        String permission = cmdInfo.permission().equals("") ? "harrypotterspells." + name : cmdInfo.permission();
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
            if (executor == null)
                sender.sendMessage(HPS.Localisation.getTranslation("cmdUnknown", s));
            else if (!sender.hasPermission(getPermission()))
                HPS.PM.dependantMessagingWarn(sender, getPermissionMessage());
            else {
                boolean success = executor.onCommand(sender, this, commandLabel, args);
                if (!success)
                    HPS.PM.dependantMessagingTell(sender, ChatColor.RED + HPS.Localisation.getTranslation("cmdUsage", s, getUsage().replace("<command>", commandLabel)));
            }
            return true;
        }
        
        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
            List<String> cmdList = new ArrayList<>(Arrays.asList("spellinfo", "teach")); //All spells
            List<String> cmdList2 = new ArrayList<>(Arrays.asList("spellswitch", "unteach")); //Player known spells
            if ((cmdList.contains(this.getName().toLowerCase()) || this.getAliases().stream().anyMatch(a -> this.getName().equalsIgnoreCase(a))) && args.length >= 1 && !HPS.SpellManager.isSpell(args[0])) {
                List<String> list = new ArrayList<String>();
                if (args[0] == null) {
                    HPS.SpellManager.getSpells().forEach(spell -> {
                        String spellName = spell.getName().replace(' ', '_');
                        list.add(spellName);
                    });
                } else {
                    HPS.SpellManager.getSpells().stream()
                    .filter(spell -> spell.getName().toLowerCase().startsWith(args[0].toLowerCase()))
                    .forEach(spell -> {
                        String spellName = spell.getName().replace(' ', '_');
                        list.add(spellName);
                    });
                }
                return list;
            } else if ((cmdList2.contains(this.getName().toLowerCase()) || this.getAliases().stream().anyMatch(a -> this.getName().equalsIgnoreCase(a))) && args.length >= 1 && !HPS.SpellManager.isSpell(args[0])) {
                List<String> list = new ArrayList<String>();
                if (args[0] == null) {
                    HPS.SpellManager.getSpells().stream()
                    .filter(spell -> spell.playerKnows((Player) sender))
                    .forEach(spell -> {
                        String spellName = spell.getName().replace(' ', '_');
                        list.add(spellName);
                    });
                } else {
                    HPS.SpellManager.getSpells().stream()
                    .filter(spell -> spell.getName().toLowerCase().startsWith(args[0].toLowerCase()) && spell.playerKnows((Player) sender))
                    .forEach(spell -> {
                        String spellName = spell.getName().replace(' ', '_');
                        list.add(spellName);
                    });
                }
                return list;
            }
            return super.tabComplete(sender, alias, args);
        }

        /**
         * Sets the executor to be used for this hacky command
         *
         * @param executor the executor
         */
        public void setExecutor(CommandExecutor executor) {
            this.executor = executor;
        }

    }

}
