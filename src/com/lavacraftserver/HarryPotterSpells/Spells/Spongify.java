package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Spongify",
		description="Prevents fall damage for 30 seconds",
		range=0,
		goThroughWalls=false
)
public class Spongify extends Spell implements Listener {
	private static List<String> players = new ArrayList<>();

	@Override
	public void cast(final Player p) {
		Spongify.players.add(p.getName());
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS.Plugin, new Runnable() {
			   public void run() {
				   if(Spongify.players.contains(p.getName())) {
					   Spongify.players.remove(p.getName());
				   } 
			   }
			}, 200L);
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if(e.getCause() == DamageCause.FALL){
			if(e.getEntity() instanceof Player){
				Player p = (Player)e.getEntity();
				if(Spongify.players.contains(p.getName())) {
					e.setDamage(0);
					Spongify.players.remove(p.getName());
				}
			}
			
		}
	}

}
