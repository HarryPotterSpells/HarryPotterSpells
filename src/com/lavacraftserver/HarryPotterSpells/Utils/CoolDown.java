package com.lavacraftserver.HarryPotterSpells.Utils;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class CoolDown extends BukkitRunnable {

	private String player;
	private Spell spell;

	public CoolDown(String playerName, Spell spellName) {
		player = playerName;
		spell = spellName;
	}

	@Override
	public void run() {
		coolDown();
	}

	public void coolDown() {
		Integer newCoolDown = HPS.SpellManager.getCoolDown(player, spell) - 1;
		if(newCoolDown == 0){
			newCoolDown = null;
		}
		HPS.SpellManager.setCoolDown(player, spell, newCoolDown);
		if(newCoolDown != null && newCoolDown >0){
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS.Plugin, new CoolDown(player, spell), 20L);
		}
	}

}
