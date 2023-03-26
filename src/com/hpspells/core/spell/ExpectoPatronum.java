package com.hpspells.core.spell;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.HPSParticle;

@SpellInfo(
        name = "Expecto Patronum",
        description = "descExpectoPatronum",
        range = 50,
        goThroughWalls = false,
        cooldown = 45
)
public class ExpectoPatronum extends Spell {

    public ExpectoPatronum(HPS instance) {
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
                EntityType e = EntityType.WITHER_SKELETON;
                if (entity.getType().equals(e)) {
                    Integer damage = (Integer) getConfig("damage", 5);
                    Double velocity = handleDouble(getConfig("velocity", 3.0), 3.0);
                    entity.damage(damage);
                    entity.setVelocity(p.getPlayer().getLocation().getDirection().multiply(velocity));
                }
            }

        }, 1, 0.5, 2, new HPSParticle(Particle.CLOUD, new DustOptions(Color.WHITE, 1)));
        return true;
    }
}
