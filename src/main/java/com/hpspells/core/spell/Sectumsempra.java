package com.hpspells.core.spell;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Sectumsempra",
        description = "descSectumsempra",
        range = 50,
        goThroughWalls = false,
        cooldown = 300
)
public class Sectumsempra extends Spell {

    public Sectumsempra(HarryPotterSpells instance) {
        super(instance);
    }

    @Override
    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellLivingEntityOnly"));
            }

            @Override
            public void hitEntity(LivingEntity entity) {
                SectumsempraRunnable sectumsemprarunnable = new SectumsempraRunnable();
                sectumsemprarunnable.le = entity;
                sectumsemprarunnable.taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(HPS, sectumsemprarunnable, 0L, 20L);

            }

        }, 1f, Particle.REDSTONE);
        return true;
    }

    private class SectumsempraRunnable implements Runnable {
        LivingEntity le;
        int taskID;
        Random random = new Random();
        int iterator = 0;
        final int length = random.nextInt(4) + 2;

        @Override
        public void run() {
            if (le.isValid())
                le.damage(1);
            else
                Bukkit.getServer().getScheduler().cancelTask(taskID);

            if (iterator < length)
                iterator++;
            else
                Bukkit.getServer().getScheduler().cancelTask(taskID);
        }

    }
}
