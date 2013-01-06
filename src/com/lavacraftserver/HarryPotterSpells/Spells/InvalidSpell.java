package com.lavacraftserver.HarryPotterSpells.Spells;


import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class InvalidSpell extends Spell {

	public InvalidSpell(HarryPotterSpells instance) {
		super(instance);
	}
	public void cast(Player p) {
		p.sendMessage(ChatColor.RED + "Invalid spell!");
	}
	@Override
	public void teach(Player sender,Player target){
		plugin.PM.warn(sender,"You cannot teach this, it's not a correct spell!");
	}
	
	@Override
	public ConfigurationSection save(ConfigurationSection c){
		return null; //this is physically impossible, an InvalidSpell never enters the list.
	}
	
	
	@Override
	public void load(ConfigurationSection c){
		plugin.getLogger().warning("Attempted to load invalid spell with display name '" + this.getName() + "'!");
	}

}
