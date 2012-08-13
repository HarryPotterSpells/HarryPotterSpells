/**
 * 
 */
package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

/**
 * @author Nate Mortensen
 *
 */
public class HPCommandDispatcher implements CommandExecutor{

	public HarryPotterSpells p;
	HPCommand[] commands;
	
	public HPCommandDispatcher(HarryPotterSpells instance){
		this.p = instance;
		commands = new HPCommand[]{new Sort(p), new Teach(p), new UnTeach(p)};
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
		//Permissions are handled by the plugin.yml
		HPCommand command = getCommand(cmd.getName());
		if (command == null){
			sender.sendMessage("There's a command that's registered to this plugin, but somehow there's no HPCommand for it.");
			return true;
		}
		command.run(sender, args, p);
		return true;
	}
	public HPCommand getCommand(String cmd){
		for (HPCommand command : commands)
			if (command.getName().equalsIgnoreCase(cmd))
				return command;
		return null;
	}

}
