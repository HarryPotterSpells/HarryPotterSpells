package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.lavacraftserver.HarryPotterSpells.HPS;

@HCommand (name="spellinfo", description="shows the description of a spell", usage="/<command> <spell>", permissionDefault="true")
public class SpellInfo implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length != 1)
			return false;
		else
			if(!HPS.SpellManager.isSpell(args[0]))
				HPS.PM.dependantMessagingWarn(sender, "That spell was not recognised");
			else
				HPS.PM.dependantMessagingTell(sender, args[0] + ": " + HPS.SpellManager.getSpell(args[0]).getDescription());
		return true;
	}
	
}
