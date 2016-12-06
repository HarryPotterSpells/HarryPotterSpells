package com.hpspells.core.command;

import com.hpspells.core.HPS;
import com.hpspells.core.configuration.ConfigurationManager.ConfigurationType;
import com.hpspells.core.configuration.PlayerSpellConfig;
import com.hpspells.core.spell.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.SortedSet;
import java.util.TreeSet;

@CommandInfo(name = "spelllist", description = "cmdSplDescription", usage = "<command> [player]", permissionDefault = "true", aliases = "sl")
public class SpellList extends HCommandExecutor {

    public SpellList(HPS instance) {
        super(instance);
        HPS.getServer().getPluginManager().addPermission(LIST_OTHERS);
    }

    public static final Permission LIST_OTHERS = new Permission("HarryPotterSpells.list.others", PermissionDefault.OP);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1)
            return false;

        if (args.length == 0) { // List all spells
            SortedSet<Spell> spells = HPS.SpellManager.getSpells();
            String spelllist = null;
            for (Spell spell : spells) {
                if (spelllist == null)
                    spelllist = "Spells: ".concat(spell.getName());
                else
                    spelllist = spelllist.concat(", " + spell.getName());
            }
            HPS.PM.dependantMessagingTell(sender, spelllist + ".");
            return true;
        }

        PlayerSpellConfig psc = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);

        if (args[0].equalsIgnoreCase("me")) { // List spells I know
            if (sender instanceof Player) {
                SortedSet<String> spells = new TreeSet<String>(psc.getStringListOrEmpty(sender.getName()));
                if (spells.size() == 0)
                    HPS.PM.tell((Player) sender, HPS.Localisation.getTranslation("genKnowNoSpells"));
                else {
                    String spellList = null;
                    for (String spell : spells) {
                        if (spellList == null)
                            spellList = (HPS.Localisation.getTranslation("cmdSplKnown") + " ").concat(spell);
                        else
                            spellList = spellList.concat(", " + spell);
                    }
                    HPS.PM.dependantMessagingTell(sender, spellList + ".");
                }
            } else
                HPS.PM.dependantMessagingTell(sender, ChatColor.RED + HPS.Localisation.getTranslation("cmdPlayerOnly"));
            return true;
        }

        // List spells someone else knows
        if (sender instanceof Player) {
            if (sender.hasPermission(LIST_OTHERS)) {
                if (Bukkit.getPlayer(args[0]) == null) {
                    HPS.PM.tell((Player) sender, HPS.Localisation.getTranslation("cmdPlayerNotFound"));
                    return true;
                }

                SortedSet<String> spells = new TreeSet<String>(psc.getStringListOrEmpty(args[0]));
                if (spells.size() == 0)
                    HPS.PM.tell((Player) sender, HPS.Localisation.getTranslation("cmdSplNoneKnown", args[0]));
                else {
                    String spellList = null;
                    for (String spell : spells) {
                        if (spellList == null)
                            spellList = (HPS.Localisation.getTranslation("cmdSplPlayerKnows", args[0]) + " ").concat(spell);
                        else
                            spellList = spellList.concat(", " + spell);
                    }
                    HPS.PM.dependantMessagingTell(sender, spellList + ".");
                }
            } else
                HPS.PM.warn((Player) sender, command.getPermissionMessage());
        } else
            HPS.PM.dependantMessagingTell(sender, ChatColor.RED + HPS.Localisation.getTranslation("cmdPlayerOnly"));

        return true;
    }
}
