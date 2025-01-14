package com.hpspells.core.command;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.spell.Spell;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

@CommandInfo(name = "unteach", description = "cmdUntDescription", usage = "<command> <spell> [player|me]")
public class Unteach extends HCommandExecutor {

    public Unteach(HarryPotterSpells instance) {
        super(instance);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 2 || args.length == 0)
            return false;

        if (!HPS.SpellManager.isSpell(args[0].replace('_', ' '))) {
            if (!(args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*"))) {
                HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("genSpellNotRecognized"));
                return false;
            }
        }

        Spell spell = HPS.SpellManager.getSpell(args[0].replace('_', ' '));
        Player unteachTo = null;

        if ((args.length == 1) || (args[1].equalsIgnoreCase("me"))) {
            if (sender instanceof Player)
                unteachTo = (Player) sender;
            else {
                HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("cmdPlayerNotSpecified"));
                return false;
            }
        } else
            unteachTo = Bukkit.getPlayer(args[1]);

        if (unteachTo != null) {
            if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*")) {
                if (HPS.SpellManager.getCurrentSpellPosition(unteachTo) == null) {
                    //Player doesn't know any spells
                    HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("cmdUntDoesntKnowAll", unteachTo.getName()));
                    return true;
                }
                HPS.SpellManager.setCurrentSpellPosition(unteachTo, 0);
                Set<Spell> spells = HPS.SpellManager.getSpells();
                String forgottenspells = null;
                for (Spell newSpell : spells) {
                    if (newSpell.playerKnows(unteachTo)) {
                        if (forgottenspells == null) {
                            newSpell.unTeach(unteachTo);
                            forgottenspells = HPS.Localisation.getTranslation("cmdUntForgot", unteachTo.getName()) + " " + newSpell.getName();
                        } else {
                            newSpell.unTeach(unteachTo);
                            forgottenspells = forgottenspells.concat(", " + newSpell.getName());
                        }
                    }
                }
                if (forgottenspells == null)
                    forgottenspells = HPS.Localisation.getTranslation("cmdUntDoesntKnowAll", unteachTo.getName());

                HPS.PM.dependantMessagingTell(sender, forgottenspells + ".");

            } else {
                if (spell.playerKnows(unteachTo)) {
                    spell.unTeach(unteachTo);
                    HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdUntForgotOne", unteachTo.getName(), spell.getName()));
                } else
                    HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("cmdUntDoesntKnowOne", unteachTo.getName(), spell.getName()));
            }
        } else {
            HPS.PM.dependantMessagingTell(sender, HPS.Localisation.getTranslation("cmdPlayerNotFound"));
            return false;
        }
        return true;
    }

}
