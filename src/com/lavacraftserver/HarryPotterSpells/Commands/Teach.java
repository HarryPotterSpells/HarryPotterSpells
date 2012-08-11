package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class Teach {
	HarryPotterSpells plugin;
	public Teach(HarryPotterSpells instance){
		plugin=instance;
	}
	
	
	public void teach(CommandSender sender, String[] args) {
		Player player = (Player)sender;
		if(args.length != 2) {
			plugin.PM.warn(player, "Correct Syntax: /teach <player> <spell>");
		} else {
			Player teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = plugin.spellManager.getSpell(args[1]);

spell.teach((Player)sender,teachTo);
			
		}
	}

	public void teachConsole(String[] args) {
		if(args.length != 2) {
			plugin.PM.log("Correct Syntax: /teach <player> <spell>", Level.INFO);
		} else {
			Player teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = plugin.spellManager.getSpell(args[1]);

			if(teachTo != null) {
				if(spell.playerKnows(teachTo)) {
					plugin.PM.log(teachTo.getName() + " already knows that spell!", Level.INFO);
				} else {
					spell.teach(teachTo);
					plugin.PM.log("You have taught " + teachTo.getName() + " the spell " + spell.toString() + ".", Level.INFO);
					
				}
			} else {
				plugin.PM.log("The player was not found.", Level.INFO);
				return;
			}
		}
	}
	
	

}
