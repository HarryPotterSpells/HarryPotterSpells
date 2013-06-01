package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
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
    
    /**
     * Registers a new {@link SpellHitEvent} to be called when a spell hits something.
     * @param caster the player who cast the spell
     * @param onHit the SpellHitEvent to call when the spell hits something
     * @param spellSpeed the vector multiplier for the movement of the spell
     * @param effect the {@link Effect} to play during the movement of the spell
     */
    public static void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final Effect effect) {
        register(caster, onHit, spellSpeed, effect, null);
    }
        
    /**
     * Registers a new {@link SpellHitEvent} to be called when a spell hits something.
     * @param caster the player who cast the spell
     * @param onHit the SpellHitEvent to call when the spell hits something
     * @param spellSpeed the vector multiplier for the movement of the spell
     * @param effect the {@link Effect} to play during the movement of the spell
     * @param effectArg the optional argument for the effect 
     */
    public static void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final Effect effect, final Integer effectArg) {
        new BukkitRunnable() {
            Location loc = caster.getEyeLocation();
            Vector direction = loc.getDirection().multiply(spellSpeed);
            boolean running = false;

            @Override
            public void run() {
                if(!running) {
                    runTaskTimer(HPS.Plugin, 0l, 1l);
                    running = true;
                }
                
                loc.add(direction);
                loc.getWorld().playEffect(loc, effect, effectArg == null ? 0 : effectArg);
                
                if(!loc.getBlock().getType().isTransparent()) {
                    onHit.hitBlock(loc.getBlock());
                    cancel();
                    return;
                }
                
                List<LivingEntity> list = getNearbyEntities(loc, 0.75, caster.getEntityId());
                if(list.size() != 0) {
                    onHit.hitEntity(list.get(0));
                    cancel();
                    return;
                } 
            }
            
        }.run();
    }
    
    /**
     * Registers a new {@link SpellHitEvent} to be called when a spell hits something.
     * @param caster the player who cast the spell
     * @param onHit the SpellHitEvent to call when the spell hits something
     * @param spellSpeed the vector multiplier for the movement of the spell
     * @param effect the {@link FireworkEffect} to play during the movement of the spell
     */
    public static void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final FireworkEffect effect) {
        new BukkitRunnable() {
            Location loc = caster.getEyeLocation();
            Vector direction = loc.getDirection().multiply(spellSpeed);
            boolean running = false;

            @Override
            public void run() {
                if(!running) {
                    runTaskTimer(HPS.Plugin, 0l, 1l);
                    running = true;
                }
                
                loc.add(direction);
                try {
                    FireworkEffectPlayer.playFirework(loc.getWorld(), loc, effect);
                } catch (Exception e) {
                    HPS.PM.log(Level.WARNING, "An error occurred whilst generating a Firework Effect!");
                    HPS.PM.debug(e);
                }
                
                if(!loc.getBlock().getType().isTransparent()) {
                    onHit.hitBlock(loc.getBlock());
                    cancel();
                    return;
                }
                
                List<LivingEntity> list = getNearbyEntities(loc, 0.75, caster.getEntityId());
                if(list.size() != 0) {
                    onHit.hitEntity(list.get(0));
                    cancel();
                    return;
                } 
            }
            
        }.run();
    }
    
    /**
     * Gets a list of LivingEntity's near a location
     * @param location the location
     * @param distance the max distance to check
     * @return
     */
    public static List<LivingEntity> getNearbyEntities(Location location, double distance, Integer... ignoreEntityId) {
        List<LivingEntity> list = new ArrayList<>();
        List<Integer> ig = Arrays.asList(ignoreEntityId);
        for(Entity en : location.getChunk().getEntities()) {
            if(!(en instanceof LivingEntity) || ig.contains(en))
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
