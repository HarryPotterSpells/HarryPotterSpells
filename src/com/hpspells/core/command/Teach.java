package com.hpspells.core.command;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

@CommandInfo(name = "teach", description = "cmdTeaDescription", usage = "<command> <spell> [player|me]")
public class Teach extends HCommandExecutor {

    public Teach(HPS instance) {
        super(instance);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 2 || args.length == 0)
            return false;

        if (!HPS.SpellManager.isSpell(args[0].replace('_', ' '))) {
            if (!(args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*"))) {
                HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("genSpellNotRecognized"));
                return true;
            }
        }

        Spell spell = HPS.SpellManager.getSpell(args[0].replace('_', ' '));
        Player teachTo = null;

        if ((args.length == 1) || (args[1].equalsIgnoreCase("me"))) {
            if (sender instanceof Player) {
                teachTo = (Player) sender;
            } else {
                HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("cmdPlayerNotSpecified"));
            }
        } else {
            teachTo = Bukkit.getPlayer(args[1]);
        }

        if (teachTo != null) {

            if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*")) {
                Set<Spell> spells = HPS.SpellManager.getSpells();
                String learnedspells = null;
                for (Spell newSpell : spells) {
                    if (!newSpell.playerKnows(teachTo)) {
                    	if (!teachTo.hasPermission(newSpell.getPermission())) {
                    		HPS.PM.dependantMessagingWarn(teachTo, HPS.Localisation.getTranslation("spellUnauthorized"));
                    		continue;
                    	}
                        if (learnedspells == null) {
                            newSpell.teach(teachTo);
                            learnedspells = HPS.Localisation.getTranslation("cmdTeaTaught", teachTo.getName()) + newSpell.getName();
                        } else {
                            newSpell.teach(teachTo);
                            learnedspells = learnedspells.concat(", " + newSpell.getName());
                        }
                    }
                }
                if (learnedspells == null) {
                    learnedspells = HPS.Localisation.getTranslation("cmdTeaKnowsAll", teachTo.getName());
                }
                HPS.PM.dependantMessagingTell(sender, learnedspells + ".");

            } else {
            	if (!teachTo.hasPermission(spell.getPermission())) {
            		HPS.PM.dependantMessagingWarn(teachTo, HPS.Localisation.getTranslation("spellUnauthorized"));
            		return true;
            	}
                if (spell.playerKnows(teachTo)) {
                    HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("cmdTeaKnowsThat", teachTo.getName()));
                } else {
                    spell.teach(teachTo);
                    HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdTeaTaughtOne", teachTo.getName(), spell.getName()));
                }

            }
        } else {
            HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("cmdPlayerNotFound"));
        }
        return true;
    }
}
