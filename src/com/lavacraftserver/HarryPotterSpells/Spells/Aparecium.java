package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Aparecium",
		description="Makes nearby players visible",
		range=0,
		goThroughWalls=false,
		cooldown=300
)

public class Aparecium extends Spell {

	public boolean cast(final Player p) {
		
		int radius = HPS.Plugin.getConfig().getInt("spells.aparecium.radius", 5);
		
		for (Entity entity : p.getNearbyEntities(radius, radius, radius)) {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				
				if (!p.canSee(player)) {
					for (Player players : Bukkit.getServer().getOnlinePlayers()) {
						players.showPlayer(player);
					}
					Location loc = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() + 1, player.getLocation().getBlockZ());
					player.getWorld().createExplosion(loc, 0, false);
				}
			}
        }
		return true;
	}
}
