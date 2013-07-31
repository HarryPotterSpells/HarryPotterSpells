package com.hpspells.core.spell;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.util.ParticleEffect;

public class Obliviate extends Spell {
	
	public Obliviate(com.hpspells.core.HPS instance) {
		super(instance);
	}

	@Override
	public boolean cast(final Player p) {
		HPS.SpellTargeter.register(p, new SpellHitEvent() {

			@Override
			public void hitBlock(Block block) {
				HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
				
			}

			@Override
			public void hitEntity(LivingEntity entity) {
				if (entity instanceof Player) {
					Player target = (Player) entity;
					HPS.SpellManager.setCoolDown(target.getName(), HPS.SpellManager.getCurrentSpell(target));
					String message = "The cooldown for the spell " + HPS.SpellManager.getCurrentSpell(target).getName() + " has been reset for " + target.getName(); 
					HPS.PM.warn(p, message);
					HPS.PM.warn(target, message);
				} else {
					HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
				}
				
			}
			
		}, 1.0, ParticleEffect.LARGE_SMOKE);
		return false;
	}

}
