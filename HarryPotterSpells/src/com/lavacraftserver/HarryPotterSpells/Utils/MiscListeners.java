package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.lavacraftserver.HarryPotterSpells.PM;

public class MiscListeners implements Listener {
	public static HashMap<String, Boolean> sonorus = new HashMap<String, Boolean>();
	public static HashMap<String, Boolean> spongify = new HashMap<String, Boolean>();
	public static HashSet<String> deprimo = new HashSet<String>();
	
	@EventHandler
	public static void onPlayerChat(PlayerChatEvent e) {
		if(sonorus.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
			PM.hps.getServer().broadcastMessage(e.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());
			sonorus.remove(e.getPlayer().getName());
		}
	}
	
	@EventHandler
	public static void onPlayerDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player && e.getCause() == DamageCause.FALL) {
			Player p = (Player)e.getEntity();
			if(spongify.containsKey(p.getName())) {
				e.setDamage(0);
				spongify.remove(p.getName());
			}
		}
	}
	
	@EventHandler
	public static void onPlayerMover(PlayerMoveEvent e) {
		if(deprimo.contains(e.getPlayer().getName())) {
			e.getPlayer().setSneaking(true);
			if(e.getFrom().getY() < e.getTo().getY()) {
				e.getPlayer().getLocation().setY(e.getFrom().getY());
			}	
		}
	}

}
