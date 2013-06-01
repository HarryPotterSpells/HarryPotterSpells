package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

@HCommand(name = "teachall", description = "Teaches a player all spells", usage = "/<command> <player|me>")
public class TeachAll implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 1)
			return false;
		
		Player teachTo;
		if (!(sender instanceof ConsoleCommandSender) && args[0].equalsIgnoreCase("me"))
			teachTo = (Player) sender;
		else
			teachTo = Bukkit.getPlayer(args[0]);

		if (teachTo != null) {
			ArrayList<Spell> spells = HPS.SpellManager.getSpells();
			String learnedspells = null;
			for (Spell spell : spells) {
				if (!spell.playerKnows(teachTo)) {
					if (learnedspells == null) {
						spell.teach(teachTo);
						learnedspells = "You have taught " + teachTo.getName() + " the spells: " + spell.getName();
					} else {
						spell.teach(teachTo);
						learnedspells = learnedspells.concat(", " + spell.getName());
					}
				}
			}
			if (learnedspells == null) {
				learnedspells = teachTo.getName() + " already knows all spells";
			}
			HPS.PM.dependantMessagingTell(sender, learnedspells);
		} else
			HPS.PM.dependantMessagingWarn(sender, "The player was not found.");

		return true;
	}

}
