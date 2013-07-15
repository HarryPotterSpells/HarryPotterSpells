package com.hpspells.core.spell;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.ParticleEffect;
import com.hpspells.core.SpellTargeter.SpellHitEvent;

@SpellInfo(
		name = "Obscuro", 
		description = "descObscuro", 
		range = 50, 
		goThroughWalls = false,
		cooldown=90
)
public class Obscuro extends Spell {
    
    public Obscuro(HPS instance) {
        super(instance);
    }

    @Override
	public boolean cast(final Player p) {
    	HPS.SpellTargeter.register(p, new SpellHitEvent() {

			@Override
			public void hitBlock(Block block) {
				HPS.PM.warn(p, HPS.Localisation.getTranslation("spellLivingEntityOnly"));
				return;
				
			}

			@Override
			public void hitEntity(LivingEntity entity) {
				if (entity instanceof Player) {

					/*
					 * TODO
					 * Not get the value from the config every spell cast;
					 * that's really resource intensive and inefficient
					 * 
					 * from Kezz101: well it's not really. Plus this method takes into account plugin/configuration reloading.
					 * Unless you want to create a ReloadJob and have every spell listen to it :/
					 * I guess that might be more resource intensive and innefficient
					 * 
					 * from zachoooo: You could always just create a configuration cache and use that. It wouls increase RAM usage but would
					 * allow you to use an already stored value instead of accessing the config everytime
					 */
					int duration = (int) getTime("duration", 400);
					entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 1));//blind them
					
					return;
				} else if(entity instanceof Creature) {
					
					((Creature) entity).setTarget(null);//erase the creatures target
					entity.setVelocity(new Vector(0,0,0));//stop them in their tracks
					
					// invert where they're looking (make them look in the opposite direction)
					Location loc = entity.getLocation();
					loc.setPitch((loc.getPitch() + 90) % 180);
					loc.setYaw((loc.getYaw()+180) % 360);
					
					entity.teleport(loc);
					
					return;
				}
				
			}
    		
    	}, 1.0, ParticleEffect.CLOUD);
		return true;
	}

}
