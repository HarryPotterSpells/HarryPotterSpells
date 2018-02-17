package com.hpspells.core.spell;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter;
import com.hpspells.core.util.ParticleEffect;

/**
 * Created by Zach on 6/26/2015.
 */

@Spell.SpellInfo(
        name = "Alohomora",
        description = "descAlohomora",
        range = 5,
        goThroughWalls = false,
        cooldown = 50
)
public class Alohomora extends Spell {

    public Alohomora(HPS instance) {
        super(instance);
    }

    @Override
    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellTargeter.SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                if (block.getType() == Material.IRON_DOOR_BLOCK) {
                    BlockState blockState = block.getState();
                    MaterialData materialData = blockState.getData();
                    Openable openable = (Openable) materialData;
                    if (openable.isOpen()) {
                        HPS.PM.warn(p, "That door is already open.");
                        return;
                    }
                    openable.setOpen(true);
                    blockState.setData(materialData);
                    blockState.update();
                } else {
                    HPS.PM.warn(p, "You may only use this spell on iron doors.");
                }

            }

            @Override
            public void hitEntity(LivingEntity entity) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellBlockOnly"));
            }

        }, 1f, ParticleEffect.SPELL_INSTANT);
        return true;
    }
}
