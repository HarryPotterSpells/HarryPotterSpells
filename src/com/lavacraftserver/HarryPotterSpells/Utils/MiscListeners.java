package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.lavacraftserver.HarryPotterSpells.HPS;

public class MiscListeners implements Listener { //TODO this class is just morally wrong
	public Set<String> sonorus = new HashSet<String>(), spongify = new HashSet<String>(), deprimo = new HashSet<String>(), petrificustotalus = new HashSet<String>();
	public Set<Integer> alarteascendare = new HashSet<Integer>();

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if(HPS.Plugin.getConfig().getBoolean("spell-castable-with-chat")) {
			if(HPS.SpellManager.isSpell(e.getMessage().substring(0, e.getMessage().length() - 1))) {
				HPS.SpellManager.getSpell(e.getMessage().substring(0, e.getMessage().length() - 1)).cast(e.getPlayer());
				return;
			}
		}

		if(sonorus.contains(e.getPlayer().getName())) {
			e.setCancelled(true);
			Bukkit.getServer().broadcastMessage(e.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());
			sonorus.remove(e.getPlayer().getName());
		}
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		String spell = e.getMessage().replace('/', ' ');
		if(HPS.SpellManager.isSpell(spell)) {
			HPS.SpellManager.getSpell(spell).cast(e.getPlayer());
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if(e.getCause() == DamageCause.FALL){
			if(e.getEntity() instanceof Player){
				Player p = (Player)e.getEntity();
				if(spongify.contains(p.getName())) {
					e.setDamage(0);
					spongify.remove(p.getName());
				}
			}else{
				if(alarteascendare.contains(e.getEntity().getEntityId())){
					e.setDamage(0);
					alarteascendare.remove(e.getEntity().getEntityId());
				}
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
	
}
