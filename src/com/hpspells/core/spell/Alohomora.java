package com.hpspells.core.spell;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter;

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
            	if (Colloportus.isLockedDoor(block)) {
                	// Assumes door is closed so unlock and force it open.
                	if (Colloportus.unlockDoor(block)) {
                		openDoor(block);
                		HPS.PM.tell(p, "That door has been unlocked.");
                	}
                } else if (block.getType() == Material.IRON_DOOR) {
                	// The way minecraft works, top door block doesnt have correct state.
                	BlockState blockState = block.getState();
                	if (((Door) blockState.getData()).isTopHalf()) {
                        blockState = block.getRelative(BlockFace.DOWN).getState();
                    }
                	Openable openable = (Openable) blockState.getData();
                    if (openable.isOpen()) {
                        HPS.PM.warn(p, "That door is already open.");
                        return;
                    }
                    openDoor(block);
                } else {
                    HPS.PM.warn(p, "You may only use this spell on iron/locked doors.");
                }
            }

            @Override
            public void hitEntity(LivingEntity entity) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellBlockOnly"));
            }

        }, 1f, Particle.SPELL_INSTANT);
        return true;
    }
    
    private void openDoor(Block block) {
    	BlockState blockState = block.getState();
    	// The way minecraft works, top door block doesnt have correct state.
        if (((Door) blockState.getData()).isTopHalf()) {
            blockState = block.getRelative(BlockFace.DOWN).getState();
        }
        MaterialData materialData = blockState.getData();
        Openable openable = (Openable) materialData;
        openable.setOpen(true);
        blockState.setData(materialData);
        blockState.update();
    }
}
