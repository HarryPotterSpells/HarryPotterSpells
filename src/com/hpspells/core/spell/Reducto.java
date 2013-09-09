package com.hpspells.core.spell;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.ParticleEffect;
import net.minecraft.server.v1_6_R1.Entity;
import net.minecraft.server.v1_6_R1.Explosion;
import net.minecraft.server.v1_6_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_6_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                    for (Block block1 : createExplosion(block.getLocation(), 4)) {
                        blocks.add(new DestroyedBlockData(block1));
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

        }, 1.0, ParticleEffect.EXPLODE);

        return true;
    }

    /**
     * Special explosion creation method to get around limitations of the Bukkit API
     *
     * @param location The location of the explosion
     * @param radius The radius of the explosion
     * @return A list of blocks affected by the explosion
     */
    public static List<Block> createExplosion(Location location, float radius) {
        World world = ((CraftWorld) location.getWorld()).getHandle();
        Explosion explosion = world.explode(null, location.getX(), location.getY(), location.getZ(), radius, false);
        return explosion.blocks;

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
