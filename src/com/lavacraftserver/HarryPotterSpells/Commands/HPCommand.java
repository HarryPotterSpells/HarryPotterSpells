/**
 * 
 */
package com.lavacraftserver.HarryPotterSpells.Commands;

import org.bukkit.command.CommandSender;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

/**
 * @author Nate Mortensen
 *
 */
public interface HPCommand {
	
	public void run(CommandSender sender, String[] args, HarryPotterSpells p);
	
	public String[] getNames();
	
	public String getPermissionNode();

}
