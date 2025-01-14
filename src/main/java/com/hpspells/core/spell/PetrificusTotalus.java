package com.hpspells.core.spell;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Petrificus Totalus",
        description = "descPetrificusTotalus",
        range = 50,
        goThroughWalls = false,
        cooldown = 300
)
public class PetrificusTotalus extends Spell implements Listener {
    public static List<String> players = new ArrayList<String>();

    public PetrificusTotalus(HarryPotterSpells instance) {
        super(instance);
    }

    @Override
    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
                return;
            }

            @Override
            public void hitEntity(LivingEntity entity) {
                if (entity instanceof Player) {
                    final Player target = (Player) entity;
                   if(!players.contains(target.getName()))  players.add(target.getName());
                    long duration = getTime("duration", 600l);

                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {

                        @Override
                        public void run() {
                            if (players.contains(target.getName()))  players.remove(target.getName());
                        }

                    }, duration);

                    Location loc = new Location(target.getWorld(), target.getLocation().getBlockX(), target.getLocation().getBlockY() + 1, target.getLocation().getBlockZ());
                    target.getWorld().createExplosion(loc, 0F);
                    return;
                } else {
                    HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
                    return;
                }

            }

        }, 1f, Particle.CRIT);
        return true;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (players.contains(e.getPlayer().getName()))
            e.setTo(e.getFrom());
    }

}
