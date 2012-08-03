package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import com.lavacraftserver.HarryPotterSpells.PM;

public class MiscListeners implements Listener {
	public static HashMap<String, Boolean> sonorus = new HashMap<String, Boolean>();
	
	@EventHandler
	public static void onPlayerChat(PlayerChatEvent e) {
		if(sonorus.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
			PM.hps.getServer().broadcastMessage(e.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());
			sonorus.remove(e.getPlayer().getName());
		}
	}

}
