package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

@HCommand(name="teach", description="Teaches a player a spell", usage="<command> <spell> [player|me]")
public class Teach implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 2 || args.length == 0)
			return false;
		
		if (!HPS.SpellManager.isSpell(args[0].replace('_', ' '))) {
			if (!(args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*"))) {
				HPS.PM.dependantMessagingWarn(sender, "That spell was not recognized.");
				return true;
			}
		}
		
		Spell spell = HPS.SpellManager.getSpell(args[0].replace('_', ' '));
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
			
			if (args[0].equalsIgnoreCase("all") || args[0].equalsIgnoreCase("*")) {
				Set<Spell> spells = HPS.SpellManager.getSpells();
				String learnedspells = null;
				for (Spell newSpell : spells) {
					if (!newSpell.playerKnows(teachTo)) {
						if (learnedspells == null) {
							newSpell.teach(teachTo);
							learnedspells = "You have taught " + teachTo.getName() + " the spells: " + newSpell.getName();
						} else {
							newSpell.teach(teachTo);
							learnedspells = learnedspells.concat(", " + newSpell.getName());
						}
					}
				}
				if (learnedspells == null) {
					learnedspells = teachTo.getName() + " already knows all spells";
				}
				HPS.PM.dependantMessagingTell(sender, learnedspells + ".");
				
			} else {
				
				if (spell.playerKnows(teachTo)) {
					HPS.PM.dependantMessagingWarn(sender, teachTo.getName() + " already knows that spell.");
				} else {
					spell.teach(teachTo);
					HPS.PM.dependantMessagingTell(sender, "You have taught " + teachTo.getName() + " the spell " + spell.getName() + ".");
				}
				
			}
		} else {
			HPS.PM.dependantMessagingWarn(sender, "The player was not found.");
		}
		return true;
	}	
}
