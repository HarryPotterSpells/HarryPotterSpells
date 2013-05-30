package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.lavacraftserver.HarryPotterSpells.HPS;

/**
 * A targeter class that targets using a thrown projectile
 */
public class SpellTargeter implements Listener {
    private static Map<Integer, SpellHitEvent> eventHitMap = new HashMap<>();
    private static Map<Integer, Integer> schedulerIdMap = new HashMap<>();
        
    /**
     * Registers a SpellHitEvent to be called when the spell has hit something
     * @param caster the player who cast the spell
     * @param projectile the projectile to be used as a spell throwable
     * @param trail the {@link FireworkEffect} to be used during the movement of the spell
     */
    public static void register(final Player caster, Class<? extends Projectile> projectile, final FireworkEffect trail, SpellHitEvent onHit) {
        final Projectile launched = caster.launchProjectile(projectile);
        launched.setShooter(caster);
        final int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(HPS.Plugin, new Runnable() {

            @Override
            public void run() {
                try {
                    FireworkEffectPlayer.playFirework(launched.getWorld(), launched.getLocation(), trail);
                } catch (Exception e) {
                    HPS.PM.log(Level.INFO, "Could not display firework trail for a spell cast by " + caster.getName() + "!");
                    HPS.PM.debug(e);
                }
            }
            
        }, 0l, 1l);
        eventHitMap.put(launched.getEntityId(), onHit);
        schedulerIdMap.put(launched.getEntityId(), id);
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
    
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if(eventHitMap.containsKey(e.getEntity().getEntityId())) {
            Bukkit.getScheduler().cancelTask(schedulerIdMap.get(e.getEntity().getEntityId()));
            if(e.getEntity().getNearbyEntities(5, 5, 5) != null && e.getEntity().getNearbyEntities(1, 1, 1).get(0) instanceof LivingEntity)
                    eventHitMap.get(e.getEntity().getEntityId()).hitEntity((LivingEntity) e.getEntity().getNearbyEntities(1, 1, 1).get(0));
            else {
                if(!e.getEntity().getLocation().getBlock().isEmpty())
                    eventHitMap.get(e.getEntity().getEntityId()).hitBlock(e.getEntity().getLocation().getBlock());
            }
            eventHitMap.remove(e.getEntity().getEntityId());
            schedulerIdMap.remove(e.getEntity().getEntityId());
        }
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
