package com.hpspells.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.hpspells.core.util.FireworkEffectPlayer;
import com.hpspells.core.util.HPSParticle;

/**
 * A targeter class that targets using a a fake projectile
 */
public class SpellTargeter {
    private HarryPotterSpells HPS;

    /**
     * Constructs an instance of {@link SpellTargeter}
     *
     * @param instance an instance of {@link HarryPotterSpells}
     */
    public SpellTargeter(HarryPotterSpells instance) {
        this.HPS = instance;
    }

    /**
     * Registers a new {@link SpellHitEvent} to be called when a spell hits something.
     *
     * @param caster     the player who cast the spell
     * @param onHit      the SpellHitEvent to call when the spell hits something
     * @param spellSpeed the vector multiplier for the movement of the spell
     * @param effect     the {@link Effect} to play during the movement of the spell
     */
    public void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final Effect effect) {
        register(caster, onHit, spellSpeed, effect, null);
    }

    /**
     * Registers a new {@link SpellHitEvent} to be called when a spell hits something.
     *
     * @param caster     the player who cast the spell
     * @param onHit      the SpellHitEvent to call when the spell hits something
     * @param spellSpeed the vector multiplier for the movement of the spell
     * @param effect     the {@link Effect} to play during the movement of the spell
     * @param effectArg  the optional argument for the effect
     */
    public void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final Effect effect, final Integer effectArg) {
        new BukkitRunnable() {
            Location loc = caster.getEyeLocation();
            Vector direction = loc.getDirection().multiply(spellSpeed);
            boolean running = false;
            long tickTracker = 0;
            long maxTicks = HPS.getConfig().getLong("spell-effective-time"); // Caching so that it doesn't lookup the value every single time

            @SuppressWarnings("deprecation")
			@Override
            public void run() {
                if (!running) {
                    runTaskTimer(HPS, 0l, 1l);
                    running = true;
                }

                loc.add(direction);
                loc.getWorld().playEffect(loc, effect, effectArg == null ? 0 : effectArg);

                if (!loc.getBlock().getType().isTransparent()) {
                    onHit.hitBlock(loc.getBlock());
                    cancel();
                    return;
                }

                List<LivingEntity> list = getNearbyEntities(loc, 2, caster);
                if (list.size() != 0) {
                    onHit.hitEntity(list.get(0));
                    cancel();
                    return;
                }
                tickTracker++;
                if (tickTracker > maxTicks && !(maxTicks == -1)) {
                    cancel();
                    return;
                }
            }

        }.run();
    }

    /**
     * Registers a new {@link SpellHitEvent} to be called when a spell hits something.
     *
     * @param caster     the player who cast the spell
     * @param onHit      the SpellHitEvent to call when the spell hits something
     * @param spellSpeed the vector multiplier for the movement of the spell
     * @param effect     the {@link FireworkEffect} to play during the movement of the spell
     */
    public void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final FireworkEffect effect) {
        new BukkitRunnable() {
            Location loc = caster.getEyeLocation();
            Vector direction = loc.getDirection().multiply(spellSpeed);
            boolean running = false;
            long tickTracker = 0;
            long maxTicks = HPS.getConfig().getLong("spell-effective-time"); // Caching so that it doesn't compare the value every single time

            @SuppressWarnings("deprecation")
			@Override
            public void run() {
                if (!running) {
                    runTaskTimer(HPS, 0l, 1l);
                    running = true;
                }

                loc.add(direction);
                try {
                    FireworkEffectPlayer.playFirework(loc.getWorld(), loc, effect);
                } catch (Exception e) {
                    HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errFireworkEffect"));
                    HPS.PM.debug(e);
                }

                if (!loc.getBlock().getType().isTransparent()) {
                    onHit.hitBlock(loc.getBlock());
                    cancel();
                    return;
                }

                List<LivingEntity> list = getNearbyEntities(loc, 2, caster);
                if (list.size() != 0) {
                    onHit.hitEntity(list.get(0));
                    cancel();
                    return;
                }
                tickTracker++;
                if (tickTracker > maxTicks && !(maxTicks == -1)) {
                    cancel();
                    return;
                }
            }

        }.run();
    }
    
    /**
     * Registers a new {@link SpellHitEvent} to be called when a spell hits something using the default offset and count.
     *
     * @param caster     the player who cast the spell
     * @param onHit      the SpellHitEvent to call when the spell hits something
     * @param spellSpeed the vector multiplier for the movement of the spell
     * @param effect     the {@link Particle} to play during the movement of the spell
     */
    public void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final Particle effect) {
        register(caster, onHit, spellSpeed, 0.5f, 1, effect);
    }

    /**
     * Registers a new {@link SpellHitEvent} to be called when a spell hits something.
     *
     * @param caster     the player who cast the spell
     * @param onHit      the SpellHitEvent to call when the spell hits something
     * @param spellSpeed the vector multiplier for the movement of the spell
     * @param offset     how far the particles should randomly offset from the center trail
     * @param count      how many particles should be displayed per tick
     * @param effect     the {@link Particle} to play during the movement of the spell
     * @see #register(Player, SpellHitEvent, double, HPSParticle) If the particle is redstone
     */
    public void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final float offset, final int count, final Particle... effect) {
        new BukkitRunnable() {
            Location loc = caster.getEyeLocation();
            Vector direction = loc.getDirection().multiply(spellSpeed);
            boolean running = false;
            long tickTracker = 0;
            long maxTicks = HPS.getConfig().getLong("spell-effective-time"); // Caching so that it doesn't compare the value every single time

            @SuppressWarnings("deprecation")
			@Override
            public void run() {
                if (!running) {
                    runTaskTimer(HPS, 0l, 1l);
                    running = true;
                }

                loc.add(direction);
                try {
                    for (Particle particle : effect) {
//                        particleEffect.display(offset, offset, offset, spellSpeed, count, loc, 25);
                    	if (particle == Particle.REDSTONE) {
                    		loc.getWorld().spawnParticle(particle, loc, count, offset, offset, offset, spellSpeed, new Particle.DustOptions(Color.RED, 1));
                    	} else {
                    		loc.getWorld().spawnParticle(particle, loc, count, offset, offset, offset, spellSpeed);
                    	}
                    }
                } catch (Exception e) {
                    HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errParticleEffect"));
                    HPS.PM.debug(e);
                }

                if (!loc.getBlock().getType().isTransparent()) {
                    onHit.hitBlock(loc.getBlock());
                    cancel();
                    return;
                }

                List<LivingEntity> list = getNearbyEntities(loc, 2, caster);
                if (list.size() != 0) {
                    onHit.hitEntity(list.get(0));
                    cancel();
                    return;
                }
                tickTracker++;
                if (tickTracker > maxTicks && !(maxTicks == -1)) {
                    cancel();
                }
            }
        }.run();
    }

    /**
     * Registers a new {@link SpellHitEvent} to be called when a spell hits something using the default offset and count.
     *
     * @param caster        the player who cast the spell
     * @param onHit         the SpellHitEvent to call when the spell hits something
     * @param spellSpeed    the vector multiplier for the movement of the spell
     * @param hpsParticle   the {@link HPSParticle} to play during the movement of the spell
     */
    public void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final HPSParticle hpsParticle) {
        register(caster, onHit, spellSpeed, 0.5f, 1, hpsParticle);
    }   
    
    /**
     * Registers a new {@link SpellHitEvent} to be called when a spell hits something.
     *
     * @param caster        the player who cast the spell
     * @param onHit         the SpellHitEvent to call when the spell hits something
     * @param spellSpeed    the vector multiplier for the movement of the spell
     * @param offset        how far the particles should randomly offset from the center trail
     * @param count         how many particles should be displayed per tick
     * @param hpsParticle   the {@link HPSParticle} to play during the movement of the spell
     */
    public void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final double offset, final int count, final HPSParticle... hpsParticles) {
        new BukkitRunnable() {
            Location loc = caster.getEyeLocation();
            Vector direction = loc.getDirection().multiply(spellSpeed);
            boolean running = false;
            long tickTracker = 0;
            long maxTicks = HPS.getConfig().getLong("spell-effective-time"); // Caching so that it doesn't compare the value every single time

            @SuppressWarnings("deprecation")
			@Override
            public void run() {
                if (!running) {
                    runTaskTimer(HPS, 0l, 1l);
                    running = true;
                }

                loc.add(direction);
                try {
                    for (HPSParticle hpsParticle : hpsParticles) {
                    	Particle particle = hpsParticle.getParticle();
                    	if (particle == Particle.REDSTONE) {
                    		DustOptions options = hpsParticle.getOptions() != null ? hpsParticle.getOptions() : new Particle.DustOptions(Color.RED, 1);
                    		loc.getWorld().spawnParticle(particle, loc, count, offset, offset, offset, spellSpeed, options);
                    	} else {
                    		loc.getWorld().spawnParticle(particle, loc, count, offset, offset, offset, spellSpeed);
                    	}
                    }
                } catch (Exception e) {
                    HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errParticleEffect"));
                    HPS.PM.debug(e);
                }

                if (!loc.getBlock().getType().isTransparent()) {
                    onHit.hitBlock(loc.getBlock());
                    cancel();
                    return;
                }

                List<LivingEntity> list = getNearbyEntities(loc, 2, caster);
                if (list.size() != 0) {
                    onHit.hitEntity(list.get(0));
                    cancel();
                    return;
                }
                tickTracker++;
                if (tickTracker > maxTicks && !(maxTicks == -1)) {
                    cancel();
                }
            }
        }.run();
    }
    
    /**
     * Gets a list of LivingEntity's near a location
     *
     * @param location the location
     * @param distance the max distance to check
     * @return
     */
    public List<LivingEntity> getNearbyEntities(Location location, double distance, Entity... ignoreEntity) {
        List<LivingEntity> list = new ArrayList<LivingEntity>();
        List<Entity> ig = Arrays.asList(ignoreEntity);
        for (Entity en : location.getChunk().getEntities()) {
            if (!(en instanceof LivingEntity) || ig.contains(en))
                continue;
            if (en.getLocation().distance(location) <= distance)
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
         *
         * @param block the block it hit
         */
        void hitBlock(Block block);

        /**
         * Called when a spell has hit an entity
         *
         * @param entity the entity it hit
         */
        void hitEntity(LivingEntity entity);

    }

}
