package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.command.CommandSender;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public abstract class HPCommand {
	
	public abstract void run(CommandSender sender, String[] args, HarryPotterSpells p);
	
	public String getName(){
		return this.getClass().getSimpleName();
	}
	

}
