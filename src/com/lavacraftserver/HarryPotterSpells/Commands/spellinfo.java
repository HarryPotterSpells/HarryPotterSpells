package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;

public class spellinfo extends Executor{

	@Override
	public String getCommand() {
		return "spellinfo";
	}
	
	public void runPlayer(Player player, String[] args) {
		if(args.length != 1) {
			HPS.PM.warn(player, "Correct Syntax: /spellinfo <spell>");
		} else {
			if(!HPS.SpellManager.isSpell(args[1])) {
				HPS.PM.warn(player, "That spell was not recognised");
				return;
			}
			HPS.PM.tell(player, args[1] + ": " + HPS.SpellManager.getSpell(args[1]).getDescription());
		}
	}
	
	public void runConsole(String[] args) {
		if(args.length != 1) {
			HPS.PM.log("Correct Syntax: /spellinfo <spell>", Level.INFO);
		} else {
			if(!HPS.SpellManager.isSpell(args[1])) {
				HPS.PM.log("That spell was not recognised", Level.WARNING);
				return;
			}
			HPS.PM.log(args[1] + ":" + HPS.SpellManager.getSpell(args[1]).getDescription(), Level.INFO);
		}
	}
}
