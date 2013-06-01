package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

@HCommand(name="teach", description="Teaches a player a spell", usage="/<command> <spell> [player|me]")
public class Teach implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 2 || args.length == 0)
			return false;
		
		if (!HPS.SpellManager.isSpell(args[0])) {
			HPS.PM.dependantMessagingWarn(sender, "That spell was not recognized.");
			return true;
		}
		
		Spell spell = HPS.SpellManager.getSpell(args[0]);
		Player teachTo = null;
		
		if ((args.length == 1) || (args[1].equalsIgnoreCase("me"))) {
			if (sender instanceof Player) {
				teachTo = (Player) sender;
			} else {
				HPS.PM.dependantMessagingWarn(sender, "Please specify a player.");
			}
		} else {
			teachTo = Bukkit.getPlayer(args[1]);
		}
	
		if (teachTo != null) {
			if (spell.playerKnows(teachTo)) {
				HPS.PM.dependantMessagingWarn(sender, teachTo.getName() + " already knows that spell.");
			} else {
				spell.teach(teachTo);
				HPS.PM.dependantMessagingTell(sender, "You have taught " + teachTo.getName() + " the spell " + spell.toString() + ".");
			}
		} else {
			HPS.PM.dependantMessagingWarn(sender, "The player was not found.");
		}
		return true;
	}	
}









