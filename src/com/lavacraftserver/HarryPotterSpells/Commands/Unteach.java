package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

@HCommand(name="unteach", description="Makes a player forget a spell", usage="/unteach <player> <spell>")
public class Unteach implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length != 2)
			return false;
			
		if(!HPS.SpellManager.isSpell(args[1])) {
			HPS.PM.dependantMessagingWarn(sender, "That spell was not recognized");
			return true;
		}
		
		Player teachTo = Bukkit.getPlayer(args[0]);
		Spell spell = HPS.SpellManager.getSpell(args[1]);
		if(teachTo != null) {
			if(spell.playerKnows(teachTo)) {
				spell.unTeach(teachTo);
				HPS.PM.dependantMessagingTell(sender, teachTo.getName() + " has forgotten " + spell.getName());
			} else
				HPS.PM.dependantMessagingWarn(sender, teachTo.getName() + " doesn't know " + spell.getName() + ".");
		} else
			HPS.PM.dependantMessagingTell(sender, "The player was not found.");
		
		return true;
	}

}
