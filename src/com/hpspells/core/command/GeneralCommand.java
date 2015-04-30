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
		usage = "<command> [config (reload|edit)]" + '\n' + "For help run /<command> [help]")
public class GeneralCommand extends HCommandExecutor {
	
    public GeneralCommand(HPS instance) {
        super(instance);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0)
            return false;

        if (args[0].equalsIgnoreCase("config")) { // Configuration editing/reloading
            if (args.length == 1)
                return false;
            else if (args[1].equalsIgnoreCase("reload")) {
                HPS.reloadConfig();
                HPS.ConfigurationManager.reloadConfigAll();
                HPS.Localisation.load();
                HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdGenConfigReloaded"));
            } else if (args[1].equalsIgnoreCase("edit")) {
                if (args.length != 5)
                    HPS.PM.dependantMessagingTell(sender, ChatColor.RED + HPS.Localisation.getTranslation("cmdUsage", (sender instanceof Player ? "/" : "") + label, " config edit <key> <new value>"));
                else {
                    if (args[2].equalsIgnoreCase("default")) {
                    	HPS.getConfig().set(args[3], args[4]);
                        HPS.saveConfig();
                        HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdGenConfigUpdated"));
                    } else if (args[2].equalsIgnoreCase("spell")) {
                    	HPS.getConfig(ConfigurationType.SPELL).get().set(args[3], args[4]);
                    	HPS.getConfig(ConfigurationType.SPELL).save();
                        HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdGenConfigSpellUpdated"));
                    } else if (args[2].equalsIgnoreCase("cooldown")) {
                    	HPS.getConfig(ConfigurationType.COOLDOWN).get().set(args[3], args[4]);
                    	HPS.getConfig(ConfigurationType.COOLDOWN).save();
                        HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdGenConfigCooldownUpdated"));
                    }
                }
            }

            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
        	HPS.PM.dependantMessagingTell(sender, "&6o0=======&c[&eHarryPotterSpells&c]&6========0o");
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
