package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class unteach extends Executor {
	
	public unteach(HarryPotterSpells instance){
		super(instance);
	}
	
	
	public void runPlayer(Player player, String[] args) {
		if(args.length != 2) {
			plugin.PM.warn(player, "Correct Syntax: /unteach <player> <spell>");
		} else {
			if(!plugin.SpellManager.isSpell(args[1])) {
				plugin.PM.warn(player, "That spell was not recognized");
				return;
			}
			Player teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = plugin.SpellManager.getSpell(args[1]);
			if(teachTo != null) {
				if(spell.playerKnows(teachTo)) {
					spell.unTeach(teachTo);
					plugin.PM.tell(player, teachTo.getName() + " has forgotten " + spell.getName() + ".");
				} else {
					plugin.PM.warn(player, teachTo.getName() + " doesn't know that spell.");
				}
			} else {
				plugin.PM.warn(player, "That player was not found.");
			}
		}
	}

	public void runConsole(String[] args) {
		if(args.length != 2) {
			plugin.PM.log("Correct Syntax: /unteach <player> <spell>", Level.INFO);
		} else {
			if(!plugin.SpellManager.isSpell(args[1])) {
				plugin.PM.log("That spell was not recognized", Level.WARNING);
				return;
			}
			Player teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = plugin.SpellManager.getSpell(args[1]);
			if(teachTo != null) {
				if(spell.playerKnows(teachTo)) {
					spell.unTeach(teachTo);
					plugin.PM.log(teachTo.getName() + " has forgotten " + spell.getName(), Level.INFO);
				} else {
					plugin.PM.log(teachTo.getName() + " doesn't know " + spell.getName() + ".", Level.INFO);
				}
			} else {
				plugin.PM.log("The player was not found.", Level.INFO);
				return;
			}
		}
	}

}
