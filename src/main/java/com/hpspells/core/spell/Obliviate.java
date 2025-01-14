package com.hpspells.core.spell;

import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hpspells.core.SpellTargeter.SpellHitEvent;

public class Obliviate extends Spell {

    public Obliviate(com.hpspells.core.HarryPotterSpells instance) {
        super(instance);
    }

    @Override
    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));

            }

            @Override
            public void hitEntity(LivingEntity entity) {
                if (entity instanceof Player) {
                    Player target = (Player) entity;
                    Spell spell = HPS.SpellManager.getCurrentSpell(target);
                    if (spell == null) {
                        HPS.PM.warn(p, "That player doesn't know any spells or has none selected!");
                    } else {
                        HPS.SpellManager.setCoolDown(target.getName(), spell);
                        String message = "The cooldown for the spell " + spell.getName() + " has been reset for " + target.getName();
                        HPS.PM.warn(p, message);
                        HPS.PM.warn(target, message);
                    }
                } else {
                    HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
                }

            }

        }, 1f, Particle.SMOKE_LARGE);
        return false;
    }

}
