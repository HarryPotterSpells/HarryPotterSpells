package com.hpspells.core.spell;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Reducto",
        description = "descReducto",
        range = 50,
        goThroughWalls = false,
        cooldown = 300
)
public class Reducto extends Spell implements Listener {
    private List<Block> sourceBlockList = new ArrayList<>();

    public Reducto(HarryPotterSpells instance) {
        super(instance);
    }

    public boolean cast(Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                if (block.getType() != Material.AIR) {
                    sourceBlockList.add(block);
                    p.getWorld().createExplosion(block.getLocation(), 4);
                }
            }

            @Override
            public void hitEntity(LivingEntity entity) {
                // TODO Auto-generated method stub
            }

        }, 1f, Particle.EXPLOSION_LARGE);

        return true;
    }

    @EventHandler
    public void onExplosion(BlockExplodeEvent event) {
        if (sourceBlockList.contains(event.getBlock())) {
            long replaceAfter = getTime("replace-blocks", 100);
            if (replaceAfter < 0) {
                return;
            }

            final List<BlockState> destroyedBlockStates = event.blockList().stream().map((Function<Block, BlockState>) block -> {
                return block.getState();
            }).collect(Collectors.toList());

            Bukkit.getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {
                @Override
                public void run() {
//                    for (DestroyedBlockData destroyedBlockData : blocks) {
//                        destroyedBlockData.getBlock().setTypeIdAndData(destroyedBlockData.getMaterial().getId(), destroyedBlockData.getData(), false);
//                    }
                    for (BlockState state : destroyedBlockStates) {
                        state.update(true);
                    }
                }
            }, replaceAfter);
        }
    }

//    /**
//     * Special explosion creation method to get around limitations of the Bukkit API
//     *
//     * @param location The location of the explosion
//     * @param radius The radius of the explosion
//     * @return A list of blocks affected by the explosion
//     */
//    public static List<Block> createExplosion(Location location, float radius) throws Exception {
//        Class<?> craftWorld = SVPBypass.getCurrentCBClass("CraftWorld");
//        Method craftWorldExplosion = SVPBypass.getMethod(craftWorld, "explode");
//
//        Object craftWorldObj = SVPBypass.getCurrentCBClass("CraftWorld").cast(location.getWorld());
//        Object explosion = craftWorldExplosion.invoke(craftWorldObj, null, location.getX(), location.getY(), location.getZ(), radius, false);
//
//        return (List<Block>) SVPBypass.getValue(explosion, "blocks");
//
//    }

//    /**
//     * Basic private class to make storing and retrieving destroyed block data easier
//     */
//    private class DestroyedBlockData {
//        private Material material;
//        private Block block;
//        private BlockData blockData;
//
//        private DestroyedBlockData(Block block) {
//            this.block = block;
//            this.material = block.getType();
//            this.blockData = block.getBlockData();
//        }
//
//        public Material getMaterial() {
//            return material;
//        }
//
//        public Block getBlock() {
//            return block;
//        }
//
//        public BlockData getBlockData() {
//            return blockData;
//        }
//    }

}
