package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Deprimo",
		description="Slows your target to an almost halt",
		range=20,
		goThroughWalls=false,
		cooldown=180
)
public class Deprimo extends Spell implements Listener {
	private static List<String> players = new ArrayList<String>();
	
	public Deprimo(HPS plugin) {
	    super(plugin);
	}

	public boolean cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			
			LivingEntity target = Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			
			int duration = (int) getTime("duration", 100l);
			
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 1));
			
			if (target instanceof Player) {
				final Player player = (Player) target;
				Deprimo.players.add(player.getName());
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {
					   
					@Override
					public void run() {
					   Deprimo.players.remove(player.getName());
					}
					   
				}, 400L);
			}
			return true;
			
		} else {
			HPS.PM.warn(p, "This can only be used on a player or mob.");
			return false;
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if(Deprimo.players.contains(e.getPlayer().getName())) {
			e.getPlayer().setSneaking(true);
			if(e.getFrom().getY() < e.getTo().getY()) {
				e.getPlayer().getLocation().setY(e.getFrom().getY());
			}	
		}
	}

}
