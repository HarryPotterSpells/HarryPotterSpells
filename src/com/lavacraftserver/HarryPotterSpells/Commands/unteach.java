package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class unteach extends Executor {
	
	@Override
	public String getCommand() {
		return "unteach";
	}
	
	public void runPlayer(Player player, String[] args) {
		if(args.length != 2) {
			HarryPotterSpells.PM.warn(player, "Correct Syntax: /unteach <player> <spell>");
		} else {
			if(!HarryPotterSpells.SpellManager.isSpell(args[1])) {
				HarryPotterSpells.PM.warn(player, "That spell was not recognized");
				return;
			}
			Player teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = HarryPotterSpells.SpellManager.getSpell(args[1]);
			if(teachTo != null) {
				if(spell.playerKnows(teachTo)) {
					spell.unTeach(teachTo);
					HarryPotterSpells.PM.tell(player, teachTo.getName() + " has forgotten " + spell.getName() + ".");
				} else {
					HarryPotterSpells.PM.warn(player, teachTo.getName() + " doesn't know that spell.");
				}
			} else {
				HarryPotterSpells.PM.warn(player, "That player was not found.");
			}
		}
	}

	public void runConsole(String[] args) {
		if(args.length != 2) {
			HarryPotterSpells.PM.log("Correct Syntax: /unteach <player> <spell>", Level.INFO);
		} else {
			if(!HarryPotterSpells.SpellManager.isSpell(args[1])) {
				HarryPotterSpells.PM.log("That spell was not recognized", Level.WARNING);
				return;
			}
			Player teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = HarryPotterSpells.SpellManager.getSpell(args[1]);
			if(teachTo != null) {
				if(spell.playerKnows(teachTo)) {
					spell.unTeach(teachTo);
					HarryPotterSpells.PM.log(teachTo.getName() + " has forgotten " + spell.getName(), Level.INFO);
				} else {
					HarryPotterSpells.PM.log(teachTo.getName() + " doesn't know " + spell.getName() + ".", Level.INFO);
				}
			} else {
				HarryPotterSpells.PM.log("The player was not found.", Level.INFO);
				return;
			}
		}
	}

}
