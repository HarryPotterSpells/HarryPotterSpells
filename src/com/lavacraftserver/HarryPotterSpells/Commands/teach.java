package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class teach extends Executor {
	
	@Override
	public String getCommand() {
		return "teach";
	}
	
	public void runPlayer(Player player, String[] args) {
		if(args.length != 2) {
			HPS.PM.warn(player, "Correct Syntax: /teach <player> <spell>");
		} else {
			if(!HPS.SpellManager.isSpell(args[1])) {
				HPS.PM.warn(player, "That spell was not recognised");
				return;
			}
			Player teachTo;
			if(args[0].equalsIgnoreCase("me"))
				teachTo = player;
			else
				teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = HPS.SpellManager.getSpell(args[1]);
			
			if(teachTo != null) {
				if(spell.playerKnows(teachTo)) {
					HPS.PM.warn(player, teachTo.getName() + " already knows that spell!");
				} else {
					spell.teach(teachTo);
					HPS.PM.tell(player, "You have taught " + teachTo.getName() + " the spell " + spell.toString() + ".");
				}
			} else {
				HPS.PM.log("The player was not found.", Level.INFO);
			}
		}
	}

	public void runConsole(String[] args) {
		if(args.length != 2) {
			HPS.PM.log("Correct Syntax: /teach <player> <spell>", Level.INFO);
		} else {
			if(!HPS.SpellManager.isSpell(args[1])) {
				HPS.PM.log("That spell was not recognised", Level.WARNING);
				return;
			}
			Player teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = HPS.SpellManager.getSpell(args[1]);

			if(teachTo != null) {
				if(spell.playerKnows(teachTo)) {
					HPS.PM.log(teachTo.getName() + " already knows that spell!", Level.INFO);
				} else {
					spell.teach(teachTo);
					HPS.PM.log("You have taught " + teachTo.getName() + " the spell " + spell.toString() + ".", Level.INFO);
					
				}
			} else {
				HPS.PM.log("The player was not found.", Level.INFO);
				return;
			}
		}
	}

}
