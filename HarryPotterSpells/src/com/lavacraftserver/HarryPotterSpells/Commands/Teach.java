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
		if(sender instanceof Player) {
			Player player = (Player)sender;
			if(args.length != 2) {
				PM.warn(player, "Correct Syntax: /teach <player> <spell>");
			} else {
				Player teachTo = Bukkit.getPlayer(args[0]);
				Spell spell = Spell.valueOf(args[1]);  
				if(spell != null && teachTo != null) {
					List<String> list = PlayerSpellConfig.getPSC().getStringList(teachTo.getName());
					list.add(spell.toString());
					PlayerSpellConfig.getPSC().set(teachTo.getName(), list);
					PM.tell(player, "You have taught " + teachTo.getName() + " the spell " + spell.toString() + ".");
					PM.tell(teachTo, "You have been taught " + spell.toString());
				} else {
					if(spell == null && teachTo == null) {PM.warn(player, "The player was not found and the spell was not recognised."); return;}
					if(spell == null) {PM.warn(player, "The spell type was not recognised."); return;}
					if(teachTo == null) {PM.warn(player, "The player was not found"); return;}
				}
			}
		} else {
			if(args.length != 2) {
				PM.log("Correct Syntax: /teach <player> <spell>", Level.INFO);
			} else {
				Player teachTo = Bukkit.getPlayer(args[0]);
				Spell spell = Spell.valueOf(args[1]);  
				if(spell != null && teachTo != null) {
					List<String> list = PlayerSpellConfig.getPSC().getStringList(teachTo.getName());
					list.add(spell.toString());
					PlayerSpellConfig.getPSC().set(teachTo.getName(), list);
					PM.log("You have taught " + teachTo.getName() + " the spell " + spell.toString() + ".", Level.INFO);
					PM.tell(teachTo, "You have been taught " + spell.toString() + "!");
				} else {
					if(spell == null && teachTo == null) {PM.log("The player was not found and the spell was not recognised.", Level.WARNING); return;}
					if(spell == null) {PM.log("The spell type was not recognised.", Level.WARNING); return;}
					if(teachTo == null) {PM.log("The player was not found", Level.WARNING); return;}
				}
			}
		}
	}

}
