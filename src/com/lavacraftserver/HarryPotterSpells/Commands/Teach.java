package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

@HCommand(name="teach", description="Teaches a player a spell", usage="/teach <player|me> <spell>")
public class Teach implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length != 2)
			return false;
			
		if(!HPS.SpellManager.isSpell(args[1])) {
			HPS.PM.dependantMessagingWarn(sender, "That spell was not recognised");
			return true;
		}
		
		Spell spell = HPS.SpellManager.getSpell(args[1]);
		Player teachTo;
		if(!(sender instanceof ConsoleCommandSender) && args[0].equalsIgnoreCase("me"))
			teachTo = (Player) sender;
		else
			teachTo = Bukkit.getPlayer(args[0]);
		
		if(teachTo != null) {
			if(spell.playerKnows(teachTo))
				HPS.PM.dependantMessagingWarn(sender, teachTo.getName() + " already knows that spell!");
			else {
				spell.teach(teachTo);
				HPS.PM.dependantMessagingTell(sender, "You have taught " + teachTo.getName() + " the spell " + spell.toString() + ".");
			}
		} else
			HPS.PM.dependantMessagingWarn(sender, "The player was not found.");
		
		return true;
	}

}
