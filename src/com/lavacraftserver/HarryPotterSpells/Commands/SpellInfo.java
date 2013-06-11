package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.lavacraftserver.HarryPotterSpells.HPS;

@HCommand (name="spellinfo", description="cmdSpiDescription", usage="<command> <spell>", permissionDefault="true")
public class SpellInfo extends HCommandExecutor {

	public SpellInfo(HPS plugin) {
        super(plugin);
    }

    @Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length != 1) {
			return false;
		} else {
			String spell = args[0].replace('_', ' ');
			if(!HPS.SpellManager.isSpell(spell))
				HPS.PM.dependantMessagingWarn(sender, HPS.Localisation.getTranslation("genSpellNotRecognized"));
			else
				HPS.PM.dependantMessagingTell(sender, spell + ": " + HPS.SpellManager.getSpell(spell).getDescription());
		    return true;
	    }
	}

}
