package com.hpspells.core.spell;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.MiscUtilities;

@SpellInfo(
        name = "Obscuro",
        description = "descObscuro",
        range = 50,
        goThroughWalls = false,
        cooldown = 90
)
public class Obscuro extends Spell {

    public Obscuro(HPS instance) {
        super(instance);
    }

    @Override
    public boolean cast(final Player p) {
        final long time = getTime((String) getConfig("duration", null), 400l);
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellLivingEntityOnly"));
                return;

            }

            @Override
            public void hitEntity(LivingEntity entity) {
                if (entity instanceof Player) {
                    int duration = (int) getTime("duration", 400);
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 1));//blind them

                    return;
                } else if (entity instanceof Creature) {
                    Creature creature = (Creature) entity;
                    creature.setTarget(null);//erase the creatures target
                    entity.setVelocity(new Vector(0, 0, 0));//stop them in their tracks

                    // invert where they're looking (make them look in the opposite direction)
                    ObscuroRunnable runnable = new ObscuroRunnable(creature, time);
                    runnable.taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(HPS, runnable, 0l, 1l);
                    Location loc = entity.getLocation();

                    entity.teleport(loc);

                    return;
                }

            }

        }, 1f, Particle.CLOUD);
        return true;
    }

    private class ObscuroRunnable implements Runnable {

        Creature creature;
        int taskId;
        int loops;
        int currentLoop;

        public ObscuroRunnable(Creature creature, long loops) {
            this.creature = creature;
            this.loops = (int) loops;
        }

        @Override
        public void run() {
            currentLoop++;
            creature.setVelocity(new Vector(0, 0, 0));
            Location loc = creature.getLocation();
            loc.setYaw(MiscUtilities.randomBetween(-180f, 180f));
            loc.setPitch(MiscUtilities.randomBetween(-90f, 90f));
            creature.teleport(loc);
            creature.setTarget(null);
            if (currentLoop > loops) {
                Bukkit.getScheduler().cancelTask(taskId);
            }
        }

    }

}
