package com.hpspells.core.spell;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.Targeter;

@SpellInfo (
		name="Petrificus Totalus",
		description="descPetrificusTotalus",
		range=50,
		goThroughWalls=false,
		cooldown=300
)
public class PetrificusTotalus extends Spell implements Listener {
	public static List<String> players = new ArrayList<String>();
	
    public PetrificusTotalus(HPS instance) {
        super(instance);
    }

	@Override
	public boolean cast(final Player p) {
		if (Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			
			players.add(((Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls())).getName());
			final Player target = (Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			long duration = getTime("duration", 600l);
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {
			    
			    @Override
				public void run() {
				    if(players.contains(target.getName())) {
				        players.remove(target.getName());
					} 
				}
				
			}, duration);
			
			Location loc = new Location(target.getWorld(), target.getLocation().getBlockX(), target.getLocation().getBlockY() + 1, target.getLocation().getBlockZ());
			target.getWorld().createExplosion(loc, 0F);
			return true;
		} else {
			HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
			return false;
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (players.contains(e.getPlayer().getName()))
			e.setTo(e.getFrom());
	}

}
