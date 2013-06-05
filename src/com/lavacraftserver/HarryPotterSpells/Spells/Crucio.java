package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.ParticleEffect;
import com.lavacraftserver.HarryPotterSpells.Utils.SpellTargeter;
import com.lavacraftserver.HarryPotterSpells.Utils.SpellTargeter.SpellHitEvent;

@spell(
		name="Crucio",
		description="Torture your victim.",
		range=50,
		goThroughWalls=false,
		cooldown=300
)

public class Crucio extends Spell implements Listener {
	private Set<String> crucioList = new HashSet<String>();
	
	public boolean cast(final Player p){
		SpellTargeter.register(p, new SpellHitEvent() {
			
			@Override
			public void hitEntity(LivingEntity entity) {
				if(entity instanceof Player) {
					final Player target = (Player) entity;
					int duration = getDuration();
					target.addPotionEffect(new PotionEffect(PotionEffectType.HARM, duration, 0));
					setFlightClever(target, true);
					target.teleport(new Location(entity.getWorld(), entity.getLocation().getX(), entity.getLocation().getBlockY()+2, entity.getLocation().getZ()));
					crucioList.add(target.getName());
					Bukkit.getScheduler().scheduleSyncDelayedTask(HPS.Plugin, new Runnable() {

						@Override
						public void run() {
							crucioList.remove(target.getName());
							setFlightClever(target, false);
						}
						
					}, (long) duration);
				} else 
					HPS.PM.warn(p, "This can only be used on a player.");
			}
			
			@Override
			public void hitBlock(Block block) {
				HPS.PM.warn(p, "This can only be used on a player.");				
			}
			
		}, 1.2d, ParticleEffect.DEPTH_SUSPEND);
		return true;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if(crucioList.contains(e.getPlayer().getName())) {
			Location changeTo = e.getFrom();
			changeTo.setPitch(e.getTo().getPitch());
			changeTo.setYaw(e.getTo().getYaw());
			e.setTo(changeTo);
		}
	}
	
	private void setFlightClever(Player player, boolean allow) {
		if(!allow && player.getGameMode() == GameMode.CREATIVE) { // If they should fly anyway
			player.setFlying(false);
			player.setAllowFlight(true);
			return;
		}
		player.setAllowFlight(allow);
		player.setFlying(allow);
	}
	
	private int getDuration() {
		String durationString = HPS.Plugin.getConfig().getString("spells.crucio.duration", "10");
		int duration = 0;

		if (durationString.endsWith("t")) {
			String ticks = durationString.substring(0, durationString.length() - 1);
			duration = Integer.parseInt(ticks);
		} else {
			duration = Integer.parseInt(durationString) * 20;
		}
		
		return duration;
	}
	
}