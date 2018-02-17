package com.hpspells.core.spell;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@SpellInfo(
        name = "Aparecium",
        description = "descAparecium",
        range = 0,
        goThroughWalls = false,
        cooldown = 300
)

public class Aparecium extends Spell {

    public Aparecium(HPS instance) {
        super(instance);
    }

    public boolean cast(final Player p) {
        int radius = (Integer) getConfig("radius", 5);
        boolean successful = false;

        for (Entity entity : p.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Player) {
                Player player = (Player) entity;

                if (!p.canSee(player)) {
                    for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                        players.showPlayer(HPS, player);
                    }
                    Location loc = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY() + 1, player.getLocation().getBlockZ());
                    player.getWorld().createExplosion(loc, 0, false);
                    successful = true;
                }
            }
        }

        return successful;
    }
}
