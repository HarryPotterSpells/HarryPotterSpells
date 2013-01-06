package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Evanesco",
		description="Makes the caster invisible",
		range=0,
		goThroughWalls=false
)
public class Evanesco extends Spell {
	
	public Evanesco(HarryPotterSpells instance) {
		super(instance);
	}

	public void cast(final Player p) {
		for (Player players : plugin.getServer().getOnlinePlayers()) {
            players.hidePlayer(p);
            p.getWorld().createExplosion(p.getLocation(), 0, false);
        }
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			   public void run() {
				   for (Player players : plugin.getServer().getOnlinePlayers()) {
			            players.showPlayer(p);
			       }
				   p.getWorld().createExplosion(p.getLocation(), 0, false);
			   }
			}, 300L);
	}

}
