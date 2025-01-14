package com.hpspells.core.spell;

import org.bukkit.Particle;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Confundo",
        description = "descConfundo",
        range = 20,
        goThroughWalls = false,
        cooldown = 180
)
public class Confundo extends Spell {

    public Confundo(HarryPotterSpells instance) {
        super(instance);
    }

    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
            	HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
            }

            @Override
            public void hitEntity(LivingEntity entity) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    p.getWorld().playSound(p, "mdc_alucicrazy", SoundCategory.AMBIENT, 1.0f, 1.0f);
                    player.getWorld().playSound(player, "mdc_alucicrazy", SoundCategory.AMBIENT, 1.0f, 1.0f);
                    long duration = getTime("duration", 200l);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (int) duration, 1));
                    return;
                } else {
                    HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
                    return;
                }

            }

        }, 1f, Particle.SMOKE_LARGE);
        return true;
    }

}
