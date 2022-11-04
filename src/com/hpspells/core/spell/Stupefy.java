package com.hpspells.core.spell;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.HPSParticle;

@SpellInfo(
        name = "Stupefy",
        description = "descStupefy",
        range = 50,
        goThroughWalls = false,
        cooldown = 120
)
public class Stupefy extends Spell {

    public Stupefy(HPS instance) {
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
            public void hitEntity(LivingEntity le) {
                le.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (int) getTime("confusion-duration", 200l), 1));
                le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (int) getTime("weakness-duration", 100l), 1));

                Vector unitVector = le.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                le.setVelocity(unitVector.multiply((Integer) getConfig("vertical-knockback", 2)));
                le.setVelocity(le.getVelocity().setY(handleDouble(getConfig("horizontal-knockback", 0.5), 0.5)));
                le.damage((Integer) getConfig("damage", 2));
            }
        }, 1.05, new HPSParticle(Particle.REDSTONE, new DustOptions(Color.RED, 1)));

        return true;
    }

}
