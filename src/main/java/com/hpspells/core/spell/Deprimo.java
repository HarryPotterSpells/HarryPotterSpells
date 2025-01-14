package com.hpspells.core.spell;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Deprimo",
        description = "descDeprimo",
        range = 20,
        goThroughWalls = false,
        cooldown = 180
)
public class Deprimo extends Spell implements Listener {
    private static List<String> players = new ArrayList<String>();

    public Deprimo(HarryPotterSpells instance) {
        super(instance);
    }

    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellLivingEntityOnly"));

            }

            @Override
            public void hitEntity(LivingEntity entity) {

                int duration = (int) getTime("duration", 100l);

                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 1));

                if (entity instanceof Player) {
                    final Player player = (Player) entity;
                    Deprimo.players.add(player.getName());
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {

                        @Override
                        public void run() {
                            Deprimo.players.remove(player.getName());
                        }

                    }, 400L);
                }
                return;

            }

        }, 1f, Particle.CRIT_MAGIC);
        return true;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (Deprimo.players.contains(e.getPlayer().getName())) {
            e.getPlayer().setSneaking(true);
            if (e.getFrom().getY() < e.getTo().getY()) {
                e.getPlayer().getLocation().setY(e.getFrom().getY());
            }
        }
    }

}
