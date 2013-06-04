package com.lavacraftserver.HarryPotterSpells.Commands;

import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Jobs.EnableJob;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

@HCommand(name = "spelllist", description = "Lists all spells", usage = "<command> [player]", permissionDefault = "true")
public class SpellList implements CommandExecutor, EnableJob {
	private final Permission LIST_OTHERS = new Permission("HarryPotterSpells.list.others", PermissionDefault.OP);
	
	@Override
	public void onEnable(PluginManager pm) {
		pm.addPermission(LIST_OTHERS);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 1)
			return false;
		
		if(args.length == 0) { // List all spells
			SortedSet<Spell> spells = HPS.SpellManager.getSpells();
			String spelllist = null;
			for (Spell spell : spells) {
				if (spelllist == null)
					spelllist = "Spells: ".concat(spell.getName());
				else
					spelllist = spelllist.concat(", " + spell.getName());
			}
			HPS.PM.dependantMessagingTell(sender, spelllist + ".");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("me")) { // List spells I know
			if(sender instanceof Player) {
				SortedSet<String> spells = new TreeSet<String>(HPS.PlayerSpellConfig.getStringListOrEmpty(sender.getName()));
				if(spells.size() == 0)
					HPS.PM.tell((Player) sender, "You do not know any spells.");
				else {
					String spellList = null;
					for (String spell : spells) {
						if (spellList == null)
							spellList = "Spells you know: ".concat(spell);
						else
							spellList = spellList.concat(", " + spell);
					}
					HPS.PM.dependantMessagingTell(sender, spellList + ".");
				}
			} else
				HPS.PM.dependantMessagingTell(sender, ChatColor.RED + "You must be a player to use this command.");
			return true;
		}
		
		// List spells someone else knows
		if(sender instanceof Player) {
			if(sender.hasPermission(LIST_OTHERS)) {
				if(Bukkit.getPlayer(args[0]) == null) {
					HPS.PM.tell((Player) sender, "That player could not be found.");
					return true;
				}
				
				SortedSet<String> spells = new TreeSet<String>(HPS.PlayerSpellConfig.getStringListOrEmpty(args[0]));
				if(spells.size() == 0)
					HPS.PM.tell((Player) sender, args[0] + " does not know any spells.");
				else {
					String spellList = null;
					for (String spell : spells) {
						if (spellList == null)
							spellList = "Spells " + args[0] + " knows: ".concat(spell);
						else
							spellList = spellList.concat(", " + spell);
					}
					HPS.PM.dependantMessagingTell(sender, spellList + ".");
				}
			} else
				HPS.PM.warn((Player) sender, command.getPermissionMessage());
		} else
			HPS.PM.dependantMessagingTell(sender, ChatColor.RED + "You must be a player to use this command.");
		
		return true;
	}
}
