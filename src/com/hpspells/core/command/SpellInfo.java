package com.hpspells.core.command;

import com.hpspells.core.HPS;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

@CommandInfo(name = "spellinfo", description = "cmdSpiDescription", usage = "<command> <spell>", permissionDefault = "true", aliases = "si")
public class SpellInfo extends HCommandExecutor {

    public SpellInfo(HPS instance) {
        super(instance);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return false;
        } else {
            String spell = args[0].replace('_', ' ');
            if (!HPS.SpellManager.isSpell(spell))
                HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("genSpellNotRecognized"));
            else
                HPS.PM.dependantMessagingTell(sender, spell + ": " + HPS.SpellManager.getSpell(spell).getDescription());
            return true;
        }
    }

}
