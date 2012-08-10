package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

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

}
