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
                    if (doorMap.containsValue(block)) 
                        HPS.PM.warn(p, "That door is already locked.");
                    BlockState blockState = block.getState();
                    MaterialData materialData = blockState.getData();
                    Openable openable = (Openable) materialData;
                    openable.setOpen(false);
                    blockState.setData(materialData);
                    blockState.update();
                    final int id_self = assignId();
                    Integer up = null, down = null;
                    HPS.PM.debug("Added door id:" + id_self);
                    doorMap.put(id_self, block);
                    Block upBlock = block.getRelative(BlockFace.UP);
                    Block downBlock = block.getRelative(BlockFace.DOWN);
                    if (doorTypes.contains(upBlock.getType()))  {
                        up = assignId();
                        HPS.PM.debug("Added door id:" + up);
                        doorMap.put(up, upBlock);
                    }
                    if (doorTypes.contains(downBlock.getType())) {
                        down = assignId();
                        HPS.PM.debug("Added door id:" + down);
                        doorMap.put(down, downBlock);
                    }
                    final Integer id_up = up;
                    final Integer id_down = down;
                    Bukkit.getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {
                        @Override
                        public void run() {
                            doorMap.remove(id_self);
                            if (id_up != null) doorMap.remove(id_up);
                            if (id_down != null) doorMap.remove(id_down);
                            HPS.PM.debug("Removed door id:" + idCounter);
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
