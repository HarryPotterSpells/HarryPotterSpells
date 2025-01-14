package com.hpspells.core.spell;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Avis",
        description = "descAvis",
        range = 0,
        goThroughWalls = false,
        cooldown = 300
)
public class Avis extends Spell {

    public Avis(HarryPotterSpells instance) {
        super(instance);
    }

    public boolean cast(Player p) {
        int chickenAmount = (Integer) getConfig("chickens.amount", 5), batAmount = (Integer) getConfig("bats.amount", 0), chickenVelocity = (Integer) getConfig("chickens.velocity", 2), batVelocity = (Integer) getConfig("bats.velocity", 2);
        for (int i = 0; i <= chickenAmount; i++) {
            Entity mob = p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.CHICKEN);
            mob.setVelocity(p.getEyeLocation().getDirection().multiply(chickenVelocity));
            randomizeVelocity(mob);
        }
        for (int i = 0; i < batAmount; i++) {
            Entity mob = p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.BAT);
            mob.setVelocity(p.getEyeLocation().getDirection().multiply(batVelocity));
            randomizeVelocity(mob);
        }
        return true;
    }

    /**
     * The numbers chosen for this are completely arbitrary and are not a result of any knowledge or testing.
     * Testing will need to be done for appropriate randomization
     */
    public void randomizeVelocity(Entity e) {
        float randomness = ((Number) getConfig("randomness", 1f)).floatValue();
        Random random = new Random();
        float randomX = (random.nextFloat() * (2 * randomness)) - randomness;
        float randomY = (random.nextFloat() * (2 * randomness)) - randomness;
        float randomZ = (random.nextFloat() * (2 * randomness)) - randomness;
        Vector v = e.getVelocity();
        v.setX(v.getX() + randomX);
        v.setY(v.getY() + randomY);
        v.setZ(v.getZ() + randomZ);
        e.setVelocity(v);
    }
}
