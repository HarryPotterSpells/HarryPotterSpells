package com.hpspells.core.spell;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.HPSParticle;

@SpellInfo(
        name = "Flipendo",
        description = "descFlipendo",
        range = 30,
        goThroughWalls = false,
        cooldown = 45
)
public class Flipendo extends Spell {

    public Flipendo(HarryPotterSpells instance) {
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
                Double velocity = handleDouble(getConfig("velocity", 2.5), 2.5);
                entity.setVelocity(p.getPlayer().getLocation().getDirection().multiply(velocity));
            }

        }, 1, 0.5, 2, new HPSParticle(Particle.CLOUD, new DustOptions(Color.AQUA, 1)));
        return true;
    }
}
