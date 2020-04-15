package com.hpspells.core.command;

import com.hpspells.core.HPS;
import com.hpspells.core.configuration.ConfigurationManager.ConfigurationType;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(
		name = "harrypotterspells", 
		description = "cmdGenDescription", 
		aliases = "hps", 
		usage = "<command> [help|reload|config]")
public class GeneralCommand extends HCommandExecutor {
	
    public GeneralCommand(HPS instance) {
        super(instance);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //If command is just /hps send usage.
        if (args.length == 0)
            return false;
        
        if (args[0].equalsIgnoreCase("config")) { // Configuration editing/reloading
            if (args.length == 1) {
                HPS.PM.dependantMessagingTell(sender, ChatColor.RED + HPS.Localisation.getTranslation("cmdUsage", (sender instanceof Player ? "/" : "") + label, " config <reload|edit>"));
                return true;
            } else if (args[1].equalsIgnoreCase("reload")) {
                if (HPS.onReload())
                    HPS.PM.dependantMessagingTell(sender, ChatColor.GREEN + HPS.Localisation.getTranslation("cmdGenConfigReloaded"));
                else 
                    HPS.PM.dependantMessagingTell(sender, "Config reloaded with errors");
            } else if (args[1].equalsIgnoreCase("edit")) {
                if (args.length != 5)
                    HPS.PM.dependantMessagingTell(sender, ChatColor.RED + HPS.Localisation.getTranslation("cmdUsage", (sender instanceof Player ? "/" : "") + label, " config edit <config_type> <key> <new value>"));
                else {
                    if (args[2].equalsIgnoreCase("default")) {
                    	if (HPS.getConfig().contains(args[3])) {
                    		HPS.getConfig().set(args[3], args[4]);
                            HPS.saveConfig();
                            HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdGenConfigUpdated"));
                    	} else {
                    		HPS.PM.dependantMessagingTell(sender, ChatColor.RED + HPS.Localisation.getTranslation("cmdUnknownConfigPaths"));
                    	}
                    } else if (args[2].equalsIgnoreCase("spell")) {
                    	if (HPS.getConfig(ConfigurationType.SPELL).get().contains(args[3])) {
                    		HPS.getConfig(ConfigurationType.SPELL).get().set(args[3], args[4]);
                        	HPS.getConfig(ConfigurationType.SPELL).save();
                            HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdGenConfigSpellUpdated"));
                    	} else {
                    		HPS.PM.dependantMessagingTell(sender, ChatColor.RED + HPS.Localisation.getTranslation("cmdUnknownConfigPaths"));
                    	}
                    } else if (args[2].equalsIgnoreCase("cooldown")) {
                    	if (HPS.getConfig(ConfigurationType.COOLDOWN).get().contains(args[3])) {
                    		HPS.getConfig(ConfigurationType.COOLDOWN).get().set(args[3], args[4]);
                        	HPS.getConfig(ConfigurationType.COOLDOWN).save();
                            HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdGenConfigCooldownUpdated"));
                    	} else {
                    		HPS.PM.dependantMessagingTell(sender, ChatColor.RED + HPS.Localisation.getTranslation("cmdUnknownConfigPaths"));
                    	}
                    } else {
                    	HPS.PM.dependantMessagingTell(sender, ChatColor.RED + HPS.Localisation.getTranslation("cmdUnknownConfigTypes","Incorrect config type. Possible config types [default, spell, cooldown]"));
                    }
                }
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (HPS.onReload()) {
                HPS.PM.dependantMessagingTell(sender, ChatColor.GREEN + HPS.Localisation.getTranslation("cmdGenPluginReloaded"));
            } else {
                HPS.PM.dependantMessagingTell(sender, ChatColor.RED + HPS.Localisation.getTranslation("errPluginReload"));
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
        	HPS.PM.dependantMessagingTell(sender, "&6o0=======&c[&eHarryPotterSpells&c]&6========0o");
        	HPS.PM.dependantMessagingTell(sender, "&b/hps &f- &e" + HPS.Localisation.getTranslation("cmdGenDescription"));
        	HPS.PM.dependantMessagingTell(sender, "&b/hps reload");
        	HPS.PM.dependantMessagingTell(sender, "&b/hps config <reload|edit>");
        	HPS.PM.dependantMessagingTell(sender, "&b/spellinfo &f- &e" + HPS.Localisation.getTranslation("cmdSpiDescription"));
        	HPS.PM.dependantMessagingTell(sender, "&b/spelllist &f- &e" + HPS.Localisation.getTranslation("cmdSplDescription"));
        	HPS.PM.dependantMessagingTell(sender, "&b/spellswitch &f- &e" + HPS.Localisation.getTranslation("cmdSpsDescription"));
        	HPS.PM.dependantMessagingTell(sender, "&b/teach &f- &e" + HPS.Localisation.getTranslation("cmdTeaDescription"));
        	HPS.PM.dependantMessagingTell(sender, "&b/unteach &f- &e" + HPS.Localisation.getTranslation("cmdUntDescription"));
        	HPS.PM.dependantMessagingTell(sender, "&b/wand &f- &e" + HPS.Localisation.getTranslation("cmdWanDescription"));
        	return true;
        }

        return false;
    }

}
