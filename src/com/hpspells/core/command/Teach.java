package com.hpspells.core.command;

import com.hpspells.core.HPS;
import com.hpspells.core.configuration.ConfigurationManager;
import com.hpspells.core.configuration.PlayerSpellConfig;
import com.hpspells.core.spell.Spell;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

@CommandInfo(name = "teach", description = "cmdTeaDescription", usage = "<command> <spell> [player|me]")
public class Teach extends HCommandExecutor {
    private final HPS HPS;

    // No need to register a permission that is not granted by default
//    private static final Permission teachKnown = new Permission("harrypotterspells.teach.known", "Restricts user to teaching only spells they know", PermissionDefault.FALSE);

    public Teach(HPS instance) {
        super(instance);
        HPS = instance;

//        instance.getServer().getPluginManager().addPermission(teachKnown);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 2 || args.length == 0) {
            return false;
        }

        if (!HPS.SpellManager.isSpell(args[0].replace('_', ' '))) {
            if (!(args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*"))) {
                HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("genSpellNotRecognized"));
                return false;
            }
        }

        Spell spell = HPS.SpellManager.getSpell(args[0].replace('_', ' '));
        Player teachTo = null;

        if ((args.length == 1) || (args[1].equalsIgnoreCase("me"))) {
            if (sender instanceof Player) {
                teachTo = (Player) sender;
            } else {
                HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("cmdPlayerNotSpecified"));
                return false;
            }
        } else {
            teachTo = Bukkit.getPlayer(args[1]);
        }

        if (teachTo != null) {
            boolean teachOnlyKnown = (sender instanceof Player) && sender.hasPermission("harrypotterspells.teach.known") && !sender.isOp();
            List<String> senderKnownSpells = ((PlayerSpellConfig) HPS.getConfig(ConfigurationManager.ConfigurationType.PLAYER_SPELL)).getStringListOrEmpty(sender.getName());
            
            HPS.PM.debug("/teach initiated by " + sender + ". teach only known: " + teachOnlyKnown);
            
            if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*")) {
                Set<Spell> spells = HPS.SpellManager.getSpells();
                String learnedSpells = null;
                boolean hasDeniedSpell = false; //If any spells get denied eg) No perm or Teach only known.

                for (Spell newSpell : spells) {
                    if (!newSpell.playerKnows(teachTo)) {
                    	if (!sender.hasPermission(newSpell.getPermission())) {
                    	    hasDeniedSpell = true;
                    		HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("spellUnauthorized") + ": " + newSpell.getName());
                    		continue;
                    	}

                        if (teachOnlyKnown && !senderKnownSpells.contains(newSpell.getName())) {
                            hasDeniedSpell = true;
                            HPS.PM.warn((Player) sender, HPS.Localisation.getTranslation("cmdTeaCantTeach", teachTo.getName(), newSpell.getName()));
                            continue;
                        }

                        if (learnedSpells == null) {
                            newSpell.teach(teachTo);
                            learnedSpells = HPS.Localisation.getTranslation("cmdTeaTaught", teachTo.getName()) + " " + newSpell.getName();
                        } else {
                            newSpell.teach(teachTo);
                            learnedSpells = learnedSpells.concat(", " + newSpell.getName());
                        }
                    }
                }

                if (learnedSpells == null) {
                    if (hasDeniedSpell) return true; //If no spells taught and spells were denied end the command here.
                    learnedSpells = HPS.Localisation.getTranslation("cmdTeaKnowsAll", teachTo.getName());
                }

                HPS.PM.dependantMessagingTell(sender, learnedSpells + ".");
            } else {
            	if (!sender.hasPermission(spell.getPermission())) {
            		HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("spellUnauthorized")+ ": " + spell.getName());
            		return true;
            	}

                if (teachOnlyKnown && !senderKnownSpells.contains(spell.getName())) {
                    HPS.PM.warn((Player) sender, HPS.Localisation.getTranslation("cmdTeaCantTeach", teachTo.getName(), spell.getName()));
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
            return false;
        }

        return true;
    }
}
