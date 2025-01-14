package com.hpspells.core.spell;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Weather",
        description = "descWeather",
        range = 0,
        goThroughWalls = false,
        cooldown = 600
)
public class Weather extends Spell {

    public Weather(HarryPotterSpells instance) {
        super(instance);
    }

    @Override
    public boolean cast(Player p) {
        p.getWorld().setWeatherDuration((int) getTime("delay", 20 * 1));
//        ParticleEffect.WATER_SPLASH.display(0.5f, 0.5f, 0.5f, 0, 1000, p.getLocation(), 1);
        p.spawnParticle(Particle.WATER_SPLASH, p.getLocation(), 1000, 0.5f, 0.5f, 0.5f, 0);
        return true;
    }
}
