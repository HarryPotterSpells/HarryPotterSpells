package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

@HCommand(name = "spelllist", description = "lists all spells", usage = "/spelllist", permissionDefault = "true")
public class SpellList implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length != 0) {
			return false;
		}
		ArrayList<Spell> spells = HPS.SpellManager.getSpells();
		String spelllist = null;
		for (Spell spell : spells) {
			if (spelllist == null) {
				spelllist = "Spells: ".concat(spell.getName());
			} else {
				spelllist = spelllist.concat(", " + spell.getName());
			}
		}
		HPS.PM.dependantMessagingTell(sender, spelllist);
		return true;
	}
}
