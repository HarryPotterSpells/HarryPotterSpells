package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class MiscListeners implements Listener {
	HarryPotterSpells plugin;
	public Set<String> sonorus = new HashSet<String>();
	public Set<String> spongify = new HashSet<String>();
	public Set<String> deprimo = new HashSet<String>();
	public Set<String> petrificustotalus = new HashSet<String>();
	
	public MiscListeners(HarryPotterSpells instance){
		plugin=instance;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if(plugin.getConfig().getBoolean("spell-castable-with-chat")) {
			// TODO yes?
		}

		if(sonorus.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
			plugin.getServer().broadcastMessage(e.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());
			sonorus.remove(e.getPlayer().getName());
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		String spell = e.getMessage().replace('/', ' ');
		if(plugin.spellManager.isSpell(spell)) {
			plugin.spellManager.getSpell(spell).cast(e.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player && e.getCause() == DamageCause.FALL) {
			Player p = (Player)e.getEntity();
			if(spongify.contains(p.getName())) {
				e.setDamage(0);
				spongify.remove(p.getName());
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if(deprimo.contains(e.getPlayer().getName())) {
			e.getPlayer().setSneaking(true);
			if(e.getFrom().getY() < e.getTo().getY()) {
				e.getPlayer().getLocation().setY(e.getFrom().getY());
			}	
		}
		if (petrificustotalus.contains(e.getPlayer().getName())) {
			e.setTo(e.getFrom());
		}
	}
	
	// Tested way of preventing players from crafting sticks
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (plugin.getConfig().getBoolean("disable-wand-crafting")) {
			Inventory inventory = event.getInventory();
			if (inventory.getType() == InventoryType.CRAFTING || inventory.getType() == InventoryType.WORKBENCH) {
				int rs = event.getRawSlot();
				ItemStack stack = null;
				if (rs >= 0) {
					stack = event.getCurrentItem();
				}
				SlotType st = event.getSlotType();

				if (st == SlotType.RESULT && stack != null) {
					if (stack.getType() == Material.getMaterial(plugin.getConfig().getInt("wand-id", 280))) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
	
}
