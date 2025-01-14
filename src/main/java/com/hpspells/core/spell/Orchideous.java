package com.hpspells.core.spell;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Orchideous",
        description = "descOrchideous",
        range = 50,
        goThroughWalls = false,
        cooldown = 45,
        icon = Material.POPPY
)
public class Orchideous extends Spell {

    public Orchideous(HarryPotterSpells instance) {
        super(instance);
    }

    @Override
    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                if (isValidBlock(block) && blockAboveIsValidBlock(block)) {
                    getBlockAbove(block).setType(Material.POPPY);
                    return;
                } else {
                    HPS.PM.warn(p, HPS.Localisation.getTranslation("spellNoRose"));
                    return;
                }
            }

            @Override
            public void hitEntity(LivingEntity entity) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellBlockOnly"));
            }

        }, 1f, Particle.HEART);
        return true;
    }

    private boolean blockAboveIsValidBlock(Block b) {
        Block blockAbove = getBlockAbove(b);
        if (blockAbove == null) {
            return false;
        }
        if (blockAbove.getType() == Material.AIR) {
            return true;
        }
        return false;
    }

    private Block getBlockAbove(Block b) {
        Location loc = b.getLocation();
        loc.setY(loc.getY() + 1);
        return loc.getBlock();
    }

    private boolean isValidBlock(Block b) {
        if (b == null) {
            return false;
        }
        switch (b.getType()) {
            case DIRT:
                return true;
            case GRASS_BLOCK:
            	return true;
            default:
                return false;
        }
    }


}
