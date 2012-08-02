package com.lavacraftserver.HarryPotterSpells;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.lavacraftserver.HarryPotterSpells.Utils.SpellSender;

public class Listeners extends JavaPlugin implements Listener {
	public static HashMap<String, Integer> currentSpell = new HashMap<String, Integer>();
	
	@EventHandler
	public void PIE(PlayerInteractEvent e) {
		if(PM.hasPermission("HarryPotterSpells.use", e.getPlayer()) && e.getPlayer().getItemInHand().getType() == Material.STICK) {
			//Change spell
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Player p = e.getPlayer();
				List<String> spellList = PlayerSpellConfig.getPSC().getStringList(p.getName());
				int spellNumber = 0, max = spellList.size() - 1;
				if(currentSpell.containsKey(p.getName())) {
					if(!(currentSpell.get(p.getName()) == max)) {
						spellNumber = currentSpell.get(p.getName()) + 1;
					}
				}
				PM.newSpell(p, spellList.get(spellNumber));
				currentSpell.put(p.getName(), spellNumber);
				return;
			}
			
			//Cast spell
			if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
				Player p = e.getPlayer();
				List<String> spellList = PlayerSpellConfig.getPSC().getStringList(p.getName());
				int spellNumber = 0;
				if(currentSpell.containsKey(p.getName())) {
					spellNumber = currentSpell.get(p.getName());
				}
				Location l = p.getLocation();
				l.setY(l.getY() + 1);
				p.getWorld().playEffect(l, Effect.ENDER_SIGNAL, 0);
				SpellSender.go(spellList.get(spellNumber), p, e);
			}
			
		}
	}
	
	@EventHandler
	public void PIEE(PlayerInteractEntityEvent e) {
		if(PM.hasPermission("HarryPotterSpells.use", e.getPlayer()) && e.getPlayer().getItemInHand().getType() == Material.STICK) {
			Player p = e.getPlayer();
			List<String> spellList = PlayerSpellConfig.getPSC().getStringList(p.getName());
			int spellNumber = 0, max = spellList.size() - 1;
			if(currentSpell.containsKey(p.getName())) {
				if(!(currentSpell.get(p.getName()) == max)) {
					spellNumber = currentSpell.get(p.getName()) + 1;
				}
			}
			PM.newSpell(p, spellList.get(spellNumber));
			currentSpell.put(p.getName(), spellNumber);
			return;
		}
	}

}
