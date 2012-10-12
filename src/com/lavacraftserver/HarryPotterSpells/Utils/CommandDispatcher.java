package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Commands.Executor;

public class CommandDispatcher implements CommandExecutor {

	public HarryPotterSpells plugin;
	
	public CommandDispatcher(HarryPotterSpells instance){
		plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String cmdAlias, String[] args) {
		String cmd = command.getName();

		try {
			Class<? extends Executor> c = Class.forName("com.lavacraftserver.HarryPotterSpells.Commands." + cmd).asSubclass(Executor.class);
			Executor executor=c.getConstructor(new Class[] {HarryPotterSpells.class}).newInstance(plugin);
			executor.run(sender, args);
			
			return true; //any syntax errors are picked up elsewhere
		} catch (Throwable e) {
			if(sender instanceof Player) {
				plugin.PM.warn((Player)sender, ChatColor.DARK_RED + "An internal error occured.");
			} else {
				plugin.PM.log("An internal error occured.", Level.WARNING);
			}
			plugin.PM.log("Couldn't handle function call for '" + cmd + "'", Level.WARNING);
			plugin.PM.debug("Message: " + e.getMessage() + ", cause: "+ e.getCause());
			
			if (plugin.getConfig().getBoolean("DebugMode")){
				e.printStackTrace();
			}
    		return true;
    	}
	}

}
