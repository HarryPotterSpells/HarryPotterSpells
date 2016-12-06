package com.hpspells.core.spell;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.inventivetalent.particle.ParticleEffect;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.SVPBypass;

@SpellInfo(
        name = "Reducto",
        description = "descReducto",
        range = 50,
        goThroughWalls = false,
        cooldown = 300
)
public class Reducto extends Spell {

    public Reducto(HPS instance) {
        super(instance);
    }

    public boolean cast(Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                if (block.getType() != Material.AIR) {
                    final List<DestroyedBlockData> blocks = new ArrayList<DestroyedBlockData>();
                    try {
                        for (Block block1 : createExplosion(block.getLocation(), 4)) {
                            blocks.add(new DestroyedBlockData(block1));
                        }
                    } catch (Exception e) {
                        // TODO catch this please!
                        return;
                    }
                    long replaceAfter = getTime("replace-blocks", 100);
                    if (replaceAfter < 0) {
                        return;
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {
                        @Override
                        public void run() {
                            for (DestroyedBlockData destroyedBlockData : blocks) {
                                destroyedBlockData.getBlock().setTypeIdAndData(destroyedBlockData.getMaterial().getId(), destroyedBlockData.getData(), false);
                            }
                        }
                    }, replaceAfter);
                }
            }

            @Override
            public void hitEntity(LivingEntity entity) {
                // TODO Auto-generated method stub

            }

        }, 1f, ParticleEffect.EXPLOSION_LARGE);

        return true;
    }

    /**
     * Special explosion creation method to get around limitations of the Bukkit API
     *
     * @param location The location of the explosion
     * @param radius The radius of the explosion
     * @return A list of blocks affected by the explosion
     */
    public static List<Block> createExplosion(Location location, float radius) throws Exception {
        Class<?> craftWorld = SVPBypass.getCurrentCBClass("CraftWorld");
        Method craftWorldExplosion = SVPBypass.getMethod(craftWorld, "explode");

        Object craftWorldObj = SVPBypass.getCurrentCBClass("CraftWorld").cast(location.getWorld());
        Object explosion = craftWorldExplosion.invoke(craftWorldObj, null, location.getX(), location.getY(), location.getZ(), radius, false);

        return (List<Block>) SVPBypass.getValue(explosion, "blocks");

    }

    /**
     * Basic private class to make storing and retrieving destroyed block data easier
     */
    private class DestroyedBlockData {
        private Material material;
        private Block block;
        private byte data;

        private DestroyedBlockData(Block block) {
            this.block = block;
            this.material = block.getType();
            this.data = block.getData();
        }

        public Material getMaterial() {
            return material;
        }

        public Block getBlock() {
            return block;
        }

        public byte getData() {
            return data;
        }
    }

}
