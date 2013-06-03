package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Spongify",
		description="Prevents fall damage",
		range=0,
		goThroughWalls=false,
		cooldown=60,
		icon=Material.SPONGE
)
public class Spongify extends Spell implements Listener {
	private List<String> players = new ArrayList<String>();

	@Override
	public boolean cast(final Player p) {
		if(players.contains(p.getName())){
			return false;
		}
		players.add(p.getName());
		Location loc = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ());
		p.getWorld().createExplosion(loc, 0F);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS.Plugin, new Runnable() {
			   public void run() {
				   if(players.contains(p.getName())) {
					   players.remove(p.getName());
				   } 
			   }
			}, HPS.Plugin.getConfig().getLong("spells.spongify.duration", 600L));
		return true;
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if(e.getCause() == DamageCause.FALL && e.getEntity() instanceof Player){
			Player p = (Player)e.getEntity();
			if(players.contains(p.getName())) {
				e.setDamage(0);
			}
			
		}
	}

}
