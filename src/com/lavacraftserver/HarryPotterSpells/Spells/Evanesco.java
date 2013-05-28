package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Bukkit;
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

	public void cast(final Player p) {
		for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            players.hidePlayer(p);
            p.getWorld().createExplosion(p.getLocation(), 0, false);
        }
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HarryPotterSpells.Plugin, new Runnable() {
			   public void run() {
				   for (Player players : Bukkit.getServer().getOnlinePlayers()) {
			            players.showPlayer(p);
			       }
				   p.getWorld().createExplosion(p.getLocation(), 0, false);
			   }
			}, 300L);
	}

}
