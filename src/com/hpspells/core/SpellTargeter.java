package com.hpspells.core;

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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.hpspells.core.util.FireworkEffectPlayer;
import com.hpspells.core.util.ParticleEffect;

/**
 * A targeter class that targets using a a fake projectile
 */
public class SpellTargeter {
	private HPS HPS;

	/**
	 * Constructs an instance of {@link SpellTargeter}
	 * @param instance an instance of {@link HPS}
	 */
	public SpellTargeter(HPS instance) {
		this.HPS = instance;
	}

	/**
	 * Registers a new {@link SpellHitEvent} to be called when a spell hits something.
	 * @param caster the player who cast the spell
	 * @param onHit the SpellHitEvent to call when the spell hits something
	 * @param spellSpeed the vector multiplier for the movement of the spell
	 * @param effect the {@link Effect} to play during the movement of the spell
	 */
	public void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final Effect effect) {
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
	public void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final Effect effect, final Integer effectArg) {
		new BukkitRunnable() {
			Location loc = caster.getEyeLocation();
			Vector direction = loc.getDirection().multiply(spellSpeed);
			boolean running = false;
			long tickTracker = 0;
			long maxTicks = HPS.getConfig().getLong("spell-effective-time"); // Caching so that it doesn't lookup the value every single time

			@Override
			public void run() {
				if(!running) {
					runTaskTimer(HPS, 0l, 1l);
					running = true;
				}

				loc.add(direction);
				loc.getWorld().playEffect(loc, effect, effectArg == null ? 0 : effectArg);

				if(!loc.getBlock().getType().isTransparent()) {
					onHit.hitBlock(loc.getBlock());
					cancel();
					return;
				}

				List<LivingEntity> list = getNearbyEntities(loc, 2, caster);
				if(list.size() != 0) {
					onHit.hitEntity(list.get(0));
					cancel();
					return;
				}
				tickTracker++;
				if (tickTracker / 20 > maxTicks && !(maxTicks == -1)) {
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
	public void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final FireworkEffect effect) {
		new BukkitRunnable() {
			Location loc = caster.getEyeLocation();
			Vector direction = loc.getDirection().multiply(spellSpeed);
			boolean running = false;
			long tickTracker = 0;
			long maxTicks = HPS.getConfig().getLong("spell-effective-time"); // Caching so that it doesn't compare the value every single time

			@Override
			public void run() {
				if(!running) {
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

				if(!loc.getBlock().getType().isTransparent()) {
					onHit.hitBlock(loc.getBlock());
					cancel();
					return;
				}

				List<LivingEntity> list = getNearbyEntities(loc, 2, caster);
				if(list.size() != 0) {
					onHit.hitEntity(list.get(0));
					cancel();
					return;
				}
				tickTracker++;
				if (tickTracker / 20 > maxTicks && !(maxTicks == -1)) {
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
	 * @param effect the {@link ParticleEffect} to play during the movement of the spell
	 * @param offset how far the particles should randomly offset from the center trail
	 * @param count how many particles should be displayed per tick
	 */
	public void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final float offset, final int count, final ParticleEffect... effect) {
		new BukkitRunnable() {
			Location loc = caster.getEyeLocation();
			Vector direction = loc.getDirection().multiply(spellSpeed);
			boolean running = false;
			long tickTracker = 0;
			long maxTicks = HPS.getConfig().getLong("spell-effective-time"); // Caching so that it doesn't compare the value every single time

			@Override
			public void run() {
				if(!running) {
					runTaskTimer(HPS, 0l, 1l);
					running = true;
				}

				loc.add(direction);
				try {
					for (ParticleEffect pe : effect) {
						ParticleEffect.sendToLocation(pe, loc, offset, offset, offset, 1f, count);
					}
				} catch (Exception e) {
					HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errParticleEffect"));
					HPS.PM.debug(e);
				}

				if(!loc.getBlock().getType().isTransparent()) {
					onHit.hitBlock(loc.getBlock());
					cancel();
					return;
				}

				List<LivingEntity> list = getNearbyEntities(loc, 2, caster);
				if(list.size() != 0) {
					onHit.hitEntity(list.get(0));
					cancel();
					return;
				}
				tickTracker++;
				if (tickTracker / 20 > maxTicks && !(maxTicks == -1)) {
					cancel();
					return;
				}
				
			}

		}.run();
	}

	/**
	 * Registers a new {@link SpellHitEvent} to be called when a spell hits something using the default offset and count.
	 * @param caster the player who cast the spell
	 * @param onHit the SpellHitEvent to call when the spell hits something
	 * @param spellSpeed the vector multiplier for the movement of the spell
	 * @param effect the {@link ParticleEffect} to play during the movement of the spell
	 */
	public void register(final Player caster, final SpellHitEvent onHit, final double spellSpeed, final ParticleEffect effect) {
		register(caster, onHit, spellSpeed, 0.5f, 1, effect);
	}

	/**
	 * Gets a list of LivingEntity's near a location
	 * @param location the location
	 * @param distance the max distance to check
	 * @return
	 */
	public List<LivingEntity> getNearbyEntities(Location location, double distance, Entity... ignoreEntity) {
		List<LivingEntity> list = new ArrayList<LivingEntity>();
		List<Entity> ig = Arrays.asList(ignoreEntity);
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
