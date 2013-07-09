package com.hpspells.core.spell;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.ParticleEffect;

@SpellInfo (
		name="Confundo",
		description="descConfundo",
		range=20,
		goThroughWalls=false,
		cooldown=180
)
public class Confundo extends Spell {
    
    public Confundo(HPS instance) {
        super(instance);
    }

    public boolean cast(Player p) {
    	final Player caster = p;
    	HPS.SpellTargeter.register(p, new SpellHitEvent() {

			@Override
			public void hitBlock(Block block) {
				// Do nothing
				
			}

			@Override
			public void hitEntity(LivingEntity entity) {
				if(entity instanceof Player) {
					Player player = (Player) entity;
					long duration = getTime("duration", 200l);
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (int) duration, 1));
					return;
				} else {
					HPS.PM.warn(caster, HPS.Localisation.getTranslation("spellPlayerOnly"));
					return;
				}
				
			}
    		
    	}, 1.0, ParticleEffect.LARGE_SMOKE);
		return true;
	}
	
}
