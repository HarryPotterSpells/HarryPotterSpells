package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.lavacraftserver.HarryPotterSpells.PM;
import com.lavacraftserver.HarryPotterSpells.PlayerSpellConfig;
import com.lavacraftserver.HarryPotterSpells.Spell;

public class Teach extends JavaPlugin {
	
	
	public static void teach(CommandSender sender, String[] args) {
		Player player = (Player)sender;
		if(args.length != 2) {
			PM.warn(player, "Correct Syntax: /teach <player> <spell>");
		} else {
			Player teachTo = Bukkit.getPlayer(args[0]);
			String stringSpell = args[1].toUpperCase();
			Spell spell;  
			try {
				spell = Spell.valueOf(stringSpell);
			} catch (IllegalArgumentException e) {
				PM.warn(player, "The spell type was not recognised!");
				return;
			}
			if(teachTo != null) {
				if(checkSpell(teachTo, spell)) {
					PM.warn(player, teachTo.getName() + " already knows that spell!");
				} else {
					List<String> list = PlayerSpellConfig.getPSC().getStringList(teachTo.getName());
					list.add(spell.toString());
					PlayerSpellConfig.getPSC().set(teachTo.getName(), list);
					PlayerSpellConfig.savePSC();
					PM.tell(player, "You have taught " + teachTo.getName() + " the spell " + spell.toString() + ".");
					PM.tell(teachTo, "You have been taught " + spell.toString());
				}
			} else {
				PM.warn(player, "The player was not found.");
				return;
			}
		}
	}

	public static void teachConsole(String[] args) {
		if(args.length != 2) {
			PM.log("Correct Syntax: /teach <player> <spell>", Level.INFO);
		} else {
			Player teachTo = Bukkit.getPlayer(args[0]);
			Spell spell = null;  
			try {
				spell = Spell.valueOf(args[1]);
			} catch (IllegalArgumentException e) {
				PM.log("The spell type was not recognised!", Level.INFO);
				return;
			}
			if(teachTo != null) {
				if(checkSpell(teachTo, spell)) {
					PM.log(teachTo.getName() + " already knows that spell!", Level.INFO);
				} else {
					List<String> list = PlayerSpellConfig.getPSC().getStringList(teachTo.getName());
					list.add(spell.toString());
					PlayerSpellConfig.getPSC().set(teachTo.getName(), list);
					PlayerSpellConfig.savePSC();
					PM.log("You have taught " + teachTo.getName() + " the spell " + spell.toString() + ".", Level.INFO);
					PM.tell(teachTo, "You have been taught " + spell.toString());
				}
			} else {
				PM.log("The player was not found.", Level.INFO);
				return;
			}
		}
	}
	
	private static boolean checkSpell(Player teachTo, Spell spell) {
		List<String> list = PlayerSpellConfig.getPSC().getStringList(teachTo.getName());
		if(list.contains(spell.toString())) {
			return true;
		} else {
			return false;
		}
	}

}
