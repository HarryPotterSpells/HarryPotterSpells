package com.hpspells.core.spell;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.ParticleEffect;
import net.minecraft.server.v1_6_R1.Explosion;
import net.minecraft.server.v1_6_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
                    final List<Block> blocks = createExplosion(block.getLocation(), 4);
                    long replaceAfter = getTime("replace-blocks", 100);
                    if (replaceAfter < 0) {
                        return;
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {
                        @Override
                        public void run() {
                            for (Block block1 : blocks) {
                                Block block2 = block1.getLocation().getBlock();
                                block2.setTypeIdAndData(block1.getTypeId(), block1.getData(), true);
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

    public static List<Block> createExplosion(Location location, float radius) {
        World world = (World) location.getWorld();
        Explosion explosion = world.createExplosion(null, location.getX(), location.getY(), location.getZ(), radius, false, false);
        return explosion.blocks;

    }

}
