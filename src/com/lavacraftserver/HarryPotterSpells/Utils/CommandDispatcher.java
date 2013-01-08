package com.lavacraftserver.HarryPotterSpells.Utils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.reflections.Reflections;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Commands.Executor;

public class CommandDispatcher implements CommandExecutor {
	public HarryPotterSpells plugin;
	Map<String, Executor> cmds = new HashMap<String, Executor>();
	
	public CommandDispatcher(HarryPotterSpells instance){
		plugin = instance;
		Reflections ref = new Reflections("com.lavacraftserver.HarryPotterSpells.Commands");
		for(Class<? extends Executor> clazz : ref.getSubTypesOf(Executor.class)) {
			Executor cmd;
			try {
				cmd = clazz.getConstructor(HarryPotterSpells.class).newInstance(plugin);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				plugin.PM.log("An error occurred whilst adding the " + clazz.getName() + " command to the command list. That command will not be available." , Level.WARNING);
				e.printStackTrace();
				continue;
			}
			cmds.put(cmd.getCommand(), cmd);
		}
	}
	
	public boolean onCommand(CommandSender sender, Command command, String cmdAlias, String[] args) {
		String cmd = command.getName().toLowerCase();
		if(cmds.get(cmd) != null)
			cmds.get(cmd).run(sender, args);
		return true;
	}

}
