package com.hpspells.core.spell;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Sonorus",
        description = "descSonorus",
        range = 0,
        goThroughWalls = false,
        cooldown = 15
)
public class Sonorus extends Spell implements Listener {
    private static List<UUID> players = new ArrayList<UUID>();

    public Sonorus(HPS instance) {
        super(instance);
    }

    public boolean cast(final Player p) {
        Sonorus.players.add(p.getUniqueId());
        Location loc = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ());
        p.getWorld().createExplosion(loc, 0F);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {

            @Override
            public void run() {
                Sonorus.players.remove(p.getUniqueId());
            }

        }, 400L);
        return true;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (Sonorus.players.contains(player.getUniqueId())) {
            e.setCancelled(true);
            HPS.PM.broadcastMessage(e.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + e.getMessage());
            Sonorus.players.remove(e.getPlayer().getUniqueId());
        }
    }

}
