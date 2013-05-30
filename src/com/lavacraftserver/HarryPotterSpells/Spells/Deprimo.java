package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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
		goThroughWalls=false
)
public class Deprimo extends Spell implements Listener {
	private static List<String> players = new ArrayList<>();

	public void cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			final Player target = (Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 500, 1));
			Deprimo.players.add(target.getName());
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS.Plugin, new Runnable() {
				   
				@Override
				public void run() {
				   Deprimo.players.remove(target.getName());
				}
				   
			}, 400L);
		} else {
			HPS.PM.warn(p, "This can only be used on a player.");
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
