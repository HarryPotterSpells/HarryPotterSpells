package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.lavacraftserver.HarryPotterSpells.HPS;

/**
 * A targeter class that targets using a thrown projectile
 */
public class SpellTargeter implements Listener {
        
    public static void register(final Player caster, final SpellHitEvent onHit, final int spellSpeed, final Effect effect, @Nullable final Integer effectArg) {
        new BukkitRunnable() {
            Location loc = caster.getLocation();
            Vector direction = loc.getDirection().multiply(spellSpeed);
            boolean running = false;

            @Override
            public void run() {
                if(!running)
                    runTaskTimer(HPS.Plugin, 0l, 1l);
                
                loc.add(direction);
                loc.getWorld().playEffect(loc, effect, effectArg == null ? 0 : effectArg);
                
                if(!getTransparentBlocks().contains(loc.getBlock().getTypeId())) {
                    onHit.hitBlock(loc.getBlock());
                    cancel();
                    return;
                }
                
                List<LivingEntity> list = getNearbyEntities(loc, 0.75);
                if(list.size() != 0) {
                    onHit.hitEntity(list.get(0));
                    cancel();
                    return;
                } 
            }
            
        }.run();
    }
    
    /**
     * Gets a list of transparent blocks
     * @return the blocks
     */
    public static HashSet<Byte> getTransparentBlocks() {
        HashSet<Byte> b = new HashSet<Byte>();
        b.add((byte) 0);
        b.add((byte) 8);
        b.add((byte) 9);
        b.add((byte) 10);
        b.add((byte) 11);
        return b;
    }
    
    /**
     * Gets a list of LivingEntity's near a location
     * @param location the location
     * @param distance the max distance to check
     * @return
     */
    public static List<LivingEntity> getNearbyEntities(Location location, double distance) {
        List<LivingEntity> list = new ArrayList<>();
        for(Entity en : location.getChunk().getEntities()) {
            if(!(en instanceof LivingEntity))
                continue;
            if(en.getLocation().distance(location) <= distance)
                list.add((LivingEntity) en);
        }
        return list;
    }
    
    /**
     * Runnable called after a spell has hit something
     */
    public interface SpellHitEvent {
        
        /**
         * Called when a spell has hit a block
         * @param block the block it hit
         */
        void hitBlock(Block block);
        
        /**
         * Called when a spell has hit an entity
         * @param entity the entity it hit
         */
        void hitEntity(LivingEntity entity);
        
    }
    
}
