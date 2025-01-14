package com.hpspells.core.spell;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter;
import com.hpspells.core.util.BlockUtils;

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

    public Alohomora(HarryPotterSpells instance) {
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
                	Block otherBlock = BlockUtils.getOtherDoorBlock(block);
//                	if (otherBlock != null) {
//                		if (isOpen(block) && !isOpen(otherBlock)) {
//                			openDoor(otherBlock);
//                			return;
//                		} else if (!isOpen(block) && isOpen(otherBlock)) {
//                			openDoor(block);
//                			return;
//                		} else if (!isOpen(block) && !isOpen(otherBlock)) {
//                			openDoor(block);
//                			openDoor(otherBlock);
//                			return;
//                		} else {
//                			HPS.PM.warn(p, "That door is already open.");
//                            return;
//                		}
//                	}
//                    if (isOpen(block)) {
//                    	HPS.PM.warn(p, "That door is already open.");
//                        return;
//                    }
                    if (isOpen(block)) {
                    	if (otherBlock != null) {
                    		if(!isOpen(otherBlock)) {
                    			openDoor(otherBlock);
                    			return;
                    		}
                    	}
                    	HPS.PM.warn(p, "That door is already open.");
                        return;
                    } else {
                    	openDoor(block);
                    	if (otherBlock != null) {
                    		openDoor(otherBlock);
                    	}
                    }
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
    
    private boolean isOpen(Block block) {
    	return ((Door) block.getBlockData()).isOpen();
    }
    
    private void openDoor(Block block) {
    	Door door = (Door) block.getBlockData();
    	door.setOpen(true);
        block.setBlockData(door);
    }
}
