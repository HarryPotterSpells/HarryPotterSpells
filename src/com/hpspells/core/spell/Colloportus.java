package com.hpspells.core.spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.ParticleEffect;

@SpellInfo(
        name = "Colloportus",
        description = "descColloportus",
        range = 50,
        goThroughWalls = false,
        cooldown = 60
)

public class Colloportus extends Spell {
    
    private static Map<Integer, Block> doorMap = new HashMap<>();
    private static int idCounter = Integer.MIN_VALUE;
    private static List<Material> doorTypes = new ArrayList<>(Arrays.asList(
            Material.WOODEN_DOOR,
            Material.IRON_DOOR_BLOCK,
            //1.8 Doors
            Material.ACACIA_DOOR,
            Material.BIRCH_DOOR,
            Material.DARK_OAK_DOOR,
            Material.JUNGLE_DOOR,
            Material.SPRUCE_DOOR
    ));
    private static List<Material> padTypes = new ArrayList<>(Arrays.asList(
            Material.WOOD_PLATE,
            Material.STONE_PLATE,
            Material.IRON_PLATE,
            Material.GOLD_PLATE
    ));
    

    public Colloportus(HPS instance) {
        super(instance);
    }

    @Override
    public boolean cast(Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
//                HPS.PM.broadcastMessage("Block type: " + block.getType());
                if (doorTypes.contains(block.getType())) {
                    if (doorMap.containsValue(block)) {
                    	HPS.PM.warn(p, "That door is already locked.");
                    	return;
                    }
                    BlockState blockState = block.getState();
                    // The way minecraft works, top door block doesnt have correct state.
                    if (((Door) blockState.getData()).isTopHalf()) {
                        blockState = block.getRelative(BlockFace.DOWN).getState();
                    }
                    Door door = (Door) blockState.getData();
                    door.setOpen(false);
                    blockState.setData(door);
                    blockState.update();
                    
                    Block otherDoorBlock = ((Door) block.getState().getData()).isTopHalf() ? block.getRelative(BlockFace.DOWN) : block.getRelative(BlockFace.UP);
                    final int blockId = assignId(), otherBlockId = assignId();
                    doorMap.put(blockId, block);
                    doorMap.put(otherBlockId, otherDoorBlock);
                    HPS.PM.debug("Added door id:" + blockId);
                    HPS.PM.debug("Added door id:" + otherBlockId);
                    
                    Bukkit.getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {
                        @Override
                        public void run() {
                        	doorMap.remove(blockId);
                        	doorMap.remove(otherBlockId);
                        	HPS.PM.debug("Removed door id:" + blockId);
                        	HPS.PM.debug("Removed door id:" + otherBlockId);
                        }
                    }, ((Integer)getConfig("lock-time", 30) * 20));
                } else {
                    HPS.PM.warn(p, "You may only use this spell on doors.");
                }
            }

            @Override
            public void hitEntity(LivingEntity entity) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellBlockOnly"));
            }
            
        }, 1f, ParticleEffect.BARRIER);
        return true;
    }
    
    public static boolean isLockedDoor(Block block) {
        if (doorMap.containsValue(block))
            return true;
        return false;
    }
    
    public static List<Material> getDoorTypes() {
        return doorTypes;
    }
    
    public static List<Material> getPadTypes() {
        return padTypes;
    }

    private Integer assignId() {
        if (idCounter == Integer.MAX_VALUE) {
            idCounter = Integer.MIN_VALUE;
        } else {
            idCounter++;
        }
        return idCounter;
    }
    
    //TODO: add double doors support
//    private boolean doorsAreConnected(Block a, Block b) {
//      if ((a.getType() != b.getType()) || isLockedDoor(a)) {
//        return false;
//      }
//      if ((a.getRelative(BlockFace.UP).getData() & 0x1) == (b.getRelative(BlockFace.UP).getData() & 0x1)) {
//        return false;
//      }
//      return (a.getData() & 0x3) == (b.getData() & 0x3);
//    }
}
