package com.hpspells.core.spell;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.ParticleEffect;

@SpellInfo (
		name="Finite Incantatum",
		description="descFiniteIncantatem",
		range=50,
		goThroughWalls=false,
		cooldown=45
		)
public class FiniteIncantatem extends Spell {

	public FiniteIncantatem(HPS instance) {
		super(instance);
	}

	@Override
	public boolean cast(final Player p) {
		HPS.SpellTargeter.register(p, new SpellHitEvent() {

			@Override
			public void hitBlock(Block block) {
				HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
				return;

			}

			@Override
			public void hitEntity(LivingEntity entity) {
				if (entity instanceof Player) {
					Player target = (Player) entity;
					PetrificusTotalus.players.remove(target.getName());

					for (PotionEffect effect : target.getActivePotionEffects()) {
						target.removePotionEffect(effect.getType());
					}

					Location loc = new Location(target.getWorld(), target.getLocation().getBlockX(), target.getLocation().getBlockY() + 1, target.getLocation().getBlockZ());
					target.getWorld().createExplosion(loc, 0F);

					return;
				}
			}

		}, 1.0, ParticleEffect.BUBBLE);
		return true;
	}
}

