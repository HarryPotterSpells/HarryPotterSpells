package com.lavacraftserver.HarryPotterSpells;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.lavacraftserver.HarryPotterSpells.Spells.SpellSender;

public class Listeners extends JavaPlugin implements Listener {
	public HashMap<String, Integer> currentSpell = new HashMap<String, Integer>();
	
	@EventHandler
	public void PIE(PlayerInteractEvent e) {
		if(PM.hasPermission("HarryPotterSpells.use", e.getPlayer()) && e.getPlayer().getItemInHand().getType() == Material.STICK) {
			//Change spell
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Player p = e.getPlayer();
				Object[] spellList = PlayerSpellConfig.getPSC().getStringList(p.getName()).toArray();
				int spellNumber = 0, max = spellList.length - 1;
				if(currentSpell.containsKey(p.getName())) {
					if(!(currentSpell.get(p.getName()) + 1 > max)) {
						spellNumber = currentSpell.get(p.getName()) + 1;
					}
				}
				PM.newSpell(p, spellList[spellNumber].toString());
				currentSpell.put(p.getName(), spellNumber);
				return;
			}
			
			//Cast spell
			if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
				Player p = e.getPlayer();
				Object[] spellList = PlayerSpellConfig.getPSC().getStringList(p.getName()).toArray();
				int currentSpellNumber = 0, max = spellList.length - 1;
				if(currentSpell.containsKey(p.getName())) {
					if(!(currentSpell.get(p.getName()) + 1 > max)) {
						currentSpellNumber = currentSpell.get(p.getName()) + 1;
					}
				}
				SpellSender.go(spellList[currentSpellNumber].toString(), p, e);
			}
			
		}
	}
	
	@EventHandler
	public void PIEE(PlayerInteractEntityEvent e) {
		if(PM.hasPermission("HarryPotterSpells.use", e.getPlayer()) && e.getPlayer().getItemInHand().getType() == Material.STICK) {
			Player p = e.getPlayer();
			Object[] spellList = PlayerSpellConfig.getPSC().getStringList(p.getName()).toArray();
			int spellNumber = 0, max = spellList.length - 1;
			if(currentSpell.containsKey(p.getName())) {
				if(!(currentSpell.get(p.getName()) + 1 > max)) {
					spellNumber = currentSpell.get(p.getName()) + 1;
				}
			}
			PM.newSpell(p, spellList[spellNumber].toString());
			currentSpell.put(p.getName(), spellNumber);
			return;
		}
	}

}
