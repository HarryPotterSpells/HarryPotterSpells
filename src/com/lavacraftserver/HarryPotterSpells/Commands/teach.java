package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class teach extends Executor {
	
	@Override
	public String getCommand() {
		return "teach";
	}
	
	public void runPlayer(Player player, String[] args) {
		if(args.length != 2) {
			HarryPotterSpells.PM.warn(player, "Correct Syntax: /teach <player> <spell>");
		} else {
			if(!HarryPotterSpells.SpellManager.isSpell(args[1])) {
				HarryPotterSpells.PM.warn(player, "That spell was not recognised");
				return;
			}
			Player teachTo;
			if(args[0].equalsIgnoreCase("me"))
				teachTo = player;
			else
				teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = HarryPotterSpells.SpellManager.getSpell(args[1]);
			
			if(teachTo != null) {
				if(spell.playerKnows(teachTo)) {
					HarryPotterSpells.PM.warn(player, teachTo.getName() + " already knows that spell!");
				} else {
					spell.teach(teachTo);
					HarryPotterSpells.PM.tell(player, "You have taught " + teachTo.getName() + " the spell " + spell.toString() + ".");
				}
			} else {
				HarryPotterSpells.PM.log("The player was not found.", Level.INFO);
			}
		}
	}

	public void runConsole(String[] args) {
		if(args.length != 2) {
			HarryPotterSpells.PM.log("Correct Syntax: /teach <player> <spell>", Level.INFO);
		} else {
			if(!HarryPotterSpells.SpellManager.isSpell(args[1])) {
				HarryPotterSpells.PM.log("That spell was not recognised", Level.WARNING);
				return;
			}
			Player teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = HarryPotterSpells.SpellManager.getSpell(args[1]);

			if(teachTo != null) {
				if(spell.playerKnows(teachTo)) {
					HarryPotterSpells.PM.log(teachTo.getName() + " already knows that spell!", Level.INFO);
				} else {
					spell.teach(teachTo);
					HarryPotterSpells.PM.log("You have taught " + teachTo.getName() + " the spell " + spell.toString() + ".", Level.INFO);
					
				}
			} else {
				HarryPotterSpells.PM.log("The player was not found.", Level.INFO);
				return;
			}
		}
	}

}
