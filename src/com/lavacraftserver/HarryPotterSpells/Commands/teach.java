package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class teach extends Executor {
	public teach(HarryPotterSpells instance){
		super(instance);
	}
	
	@Override
	public String getCommand() {
		return "teach";
	}
	
	public void runPlayer(Player player, String[] args) {
		if(args.length != 2) {
			plugin.PM.warn(player, "Correct Syntax: /teach <player> <spell>");
		} else {
			if(!plugin.SpellManager.isSpell(args[1])) {
				plugin.PM.warn(player, "That spell was not recognised");
				return;
			}
			Player teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = plugin.SpellManager.getSpell(args[1]);
			spell.teach(player,teachTo);
		}
	}

	public void runConsole(String[] args) {
		if(args.length != 2) {
			plugin.PM.log("Correct Syntax: /teach <player> <spell>", Level.INFO);
		} else {
			if(!plugin.SpellManager.isSpell(args[1])) {
				plugin.PM.log("That spell was not recognised", Level.WARNING);
				return;
			}
			Player teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = plugin.SpellManager.getSpell(args[1]);

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
