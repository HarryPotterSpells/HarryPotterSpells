package com.lavacraftserver.HarryPotterSpells.Utils;

import java.lang.reflect.Method;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class CommandDispatcher implements CommandExecutor {

	public HarryPotterSpells plugin;
	
	public CommandDispatcher(HarryPotterSpells instance){
		plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String cmdAlias, String[] args) {
		String cmd = command.getName();

		try {
			Class<?>[] proto = new Class[] {CommandSender.class, String.class, String[].class};
			Object[] params = new Object[] {sender, cmdAlias, args};
			Class<?> c = Class.forName("com.lavacraftserver.HarryPotterSpells.Commands." + cmd);
			Method method = c.getDeclaredMethod("run", proto);
			Object ret = method.invoke(null, params);
			return Boolean.TRUE.equals(ret);
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
