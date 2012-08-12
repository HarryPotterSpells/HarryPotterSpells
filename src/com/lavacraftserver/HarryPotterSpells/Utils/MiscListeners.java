package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class MiscListeners implements Listener {
	HarryPotterSpells plugin;
	public MiscListeners(HarryPotterSpells instance){
		plugin=instance;
	}
	public Set<String> sonorus = new HashSet<String>();
	public Set<String> spongify = new HashSet<String>();
	public Set<String> deprimo = new HashSet<String>();
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if(plugin.getConfig().getBoolean("spell-castable-with-chat")) {

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
		if(plugin.spellManager.getSpell(spell) != null) {
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
	}
	
	@EventHandler
	public void onPlayerCraft(CraftItemEvent e) {
		if(e.getRecipe().getResult().getType() == Material.STICK && plugin.getConfig().getBoolean("disable-stick-crafting")) {
			e.setResult(Result.DENY);
		}
	}

}
