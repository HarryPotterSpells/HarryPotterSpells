package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.lavacraftserver.HarryPotterSpells.HPS;

@HCommand(name="harrypotterspells", description="Command for controlling some aspects of the plugin in-game", aliases="hps", usage="<command> [reload]")
public class GeneralCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0)
            return false;
        
        if(args[0].equalsIgnoreCase("reload")) {
            HPS.Plugin.reloadConfig();
            HPS.PM.dependantMessagingTell(sender, "Successfully reloaded the config.");
            return true;
        }
        
        return false;
    }
    
}
