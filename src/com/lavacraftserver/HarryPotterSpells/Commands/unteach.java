package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class unteach extends Executor {
	
	@Override
	public String getCommand() {
		return "unteach";
	}
	
	public void runPlayer(Player player, String[] args) {
		if(args.length != 2) {
			HPS.PM.warn(player, "Correct Syntax: /unteach <player> <spell>");
		} else {
			if(!HPS.SpellManager.isSpell(args[1])) {
				HPS.PM.warn(player, "That spell was not recognized");
				return;
			}
			Player teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = HPS.SpellManager.getSpell(args[1]);
			if(teachTo != null) {
				if(spell.playerKnows(teachTo)) {
					spell.unTeach(teachTo);
					HPS.PM.tell(player, teachTo.getName() + " has forgotten " + spell.getName() + ".");
				} else {
					HPS.PM.warn(player, teachTo.getName() + " doesn't know that spell.");
				}
			} else {
				HPS.PM.warn(player, "That player was not found.");
			}
		}
	}

	public void runConsole(String[] args) {
		if(args.length != 2) {
			HPS.PM.log("Correct Syntax: /unteach <player> <spell>", Level.INFO);
		} else {
			if(!HPS.SpellManager.isSpell(args[1])) {
				HPS.PM.log("That spell was not recognized", Level.WARNING);
				return;
			}
			Player teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = HPS.SpellManager.getSpell(args[1]);
			if(teachTo != null) {
				if(spell.playerKnows(teachTo)) {
					spell.unTeach(teachTo);
					HPS.PM.log(teachTo.getName() + " has forgotten " + spell.getName(), Level.INFO);
				} else {
					HPS.PM.log(teachTo.getName() + " doesn't know " + spell.getName() + ".", Level.INFO);
				}
			} else {
				HPS.PM.log("The player was not found.", Level.INFO);
				return;
			}
		}
	}

}
