package com.lavacraftserver.HarryPotterSpells;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;



public class Listeners implements Listener {
	public HashMap<String, Integer> currentSpell = new HashMap<String, Integer>();
	HarryPotterSpells plugin;
	public Listeners(HarryPotterSpells instance){
		plugin=instance;
	}
	@EventHandler
	public void PIE(PlayerInteractEvent e) {
		if(plugin.PM.hasPermission("HarryPotterSpells.use", e.getPlayer()) && e.getPlayer().getItemInHand().getType() == Material.STICK) {
			//Change spell
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Player p = e.getPlayer();
				List<String> spellList = plugin.PlayerSpellConfig.getPSC().getStringList(p.getName());
				if(spellList == null || spellList.isEmpty()) {
					plugin.PM.tell(p, "You don't know any spells.");
					return;
				}
				int spellNumber = 0, max = spellList.size() - 1;
				if(currentSpell.containsKey(p.getName())) {
					if(p.isSneaking()) {
						if(currentSpell.get(p.getName()) == 0) {
							spellNumber = max;
						} else {
							spellNumber = currentSpell.get(p.getName()) - 1;
						}
					} else if(!(currentSpell.get(p.getName()) == max)) {
						spellNumber = currentSpell.get(p.getName()) + 1;
					}
				}
				plugin.PM.newSpell(p, spellList.get(spellNumber));
				currentSpell.put(p.getName(), spellNumber);
				return;
			}
			
			//Cast spell
			if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
				Player p = e.getPlayer();
				List<String> spellList = plugin.PlayerSpellConfig.getPSC().getStringList(p.getName());
				if(spellList == null || spellList.isEmpty()) {
					plugin.PM.tell(p, "You don't know any spells.");
					return;
				}
				int spellNumber = 0;
				if(currentSpell.containsKey(p.getName())) {
					spellNumber = currentSpell.get(p.getName());
				}
				Location l = p.getLocation();
				l.setY(l.getY() + 1);
				p.getWorld().playEffect(l, Effect.ENDER_SIGNAL, 0);
				plugin.spellManager.getSpell(spellList.get(spellNumber)).cast(e.getPlayer());
				plugin.LogBlock.logSpell(p,spellList.get(spellNumber));
				//Cancel event if player is in creative to prevent block damage.
				if (p.getGameMode().equals(GameMode.CREATIVE)){
					e.setCancelled(true);
				}
			}
			
		}
	}
	
	@EventHandler
	public void PIEE(PlayerInteractEntityEvent e) {
		if(plugin.PM.hasPermission("HarryPotterSpells.use", e.getPlayer()) && e.getPlayer().getItemInHand().getType() == Material.STICK) {
			Player p = e.getPlayer();
			List<String> spellList = plugin.PlayerSpellConfig.getPSC().getStringList(p.getName());
			if(spellList == null || spellList.isEmpty()) {
				plugin.PM.tell(p, "You don't know any spells.");
				return;
			}
			int spellNumber = 0, max = spellList.size() - 1;
			if(currentSpell.containsKey(p.getName())) {
				if(p.isSneaking()) {
					if(currentSpell.get(p.getName()) == 0) {
						spellNumber = max;
					} else {
						spellNumber = currentSpell.get(p.getName()) - 1;
					}
				} else if(!(currentSpell.get(p.getName()) == max)) {
					spellNumber = currentSpell.get(p.getName()) + 1;
				}
			}
			plugin.PM.newSpell(p, spellList.get(spellNumber));
			currentSpell.put(p.getName(), spellNumber);
			return;
		}
	}

}
