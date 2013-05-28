package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public abstract class Executor implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		run(sender,args);
		return false;
	}
	
	public void run(CommandSender sender, String[] args) {
		if (sender instanceof Player)
			runPlayer((Player)sender, args);
		else
			runConsole(args);
	}
	
	public abstract String getCommand();
	
	/*
	 * Override the functions below depending on who can run the command
	 */
	
	public void runPlayer(Player sender, String[] args) { //TODO this whole run console crap is pointless
		HarryPotterSpells.PM.log("This command cannot be run from in game.", Level.WARNING);
	}
	
	public void runConsole(String[] args){
		HarryPotterSpells.PM.log("This command cannot be run from the console.",Level.WARNING);
	}
	
}
