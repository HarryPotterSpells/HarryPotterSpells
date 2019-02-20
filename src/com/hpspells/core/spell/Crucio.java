package com.hpspells.core.spell;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Crucio",
        description = "descCrucio",
        range = 50,
        goThroughWalls = false,
        cooldown = 300
)

public class Crucio extends Spell implements Listener {
    private Set<String> crucioList = new HashSet<String>();

    public Crucio(HPS instance) {
        super(instance);
    }

    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitEntity(LivingEntity entity) {
                if (entity instanceof Player) {
                    final Player target = (Player) entity;
                    long duration = getTime("duration", 200l);
                    target.addPotionEffect(new PotionEffect(PotionEffectType.HARM, (int) duration, 0));
                    setFlightClever(target, true);
                    target.teleport(new Location(entity.getWorld(), entity.getLocation().getX(), entity.getLocation().getBlockY() + 2, entity.getLocation().getZ()));
                    crucioList.add(target.getName());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {

                        @Override
                        public void run() {
                            crucioList.remove(target.getName());
                            setFlightClever(target, false);
                        }

                    }, duration);
                } else
                    HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
            }

            @Override
            public void hitBlock(Block block) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
            }

        }, 1.2f, Particle.SUSPENDED_DEPTH);
        return true;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (crucioList.contains(e.getPlayer().getName())) {
            Location changeTo = e.getFrom();
            changeTo.setPitch(e.getTo().getPitch());
            changeTo.setYaw(e.getTo().getYaw());
            e.setTo(changeTo);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.PLAYER && crucioList.contains(((Player) e.getEntity()).getName())) {
            Player p = (Player) e.getEntity();
            if ((p.getHealth() - e.getDamage()) < 1)
                e.setDamage(0);
        }
    }

    private void setFlightClever(Player player, boolean allow) {
        if (!allow && player.getGameMode() == GameMode.CREATIVE) { // If they should fly anyway
            player.setFlying(false);
            player.setAllowFlight(true);
            return;
        }
        player.setAllowFlight(allow);
        player.setFlying(allow);
    }

}
