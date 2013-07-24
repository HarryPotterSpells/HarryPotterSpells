package com.hpspells.core.spell;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.ParticleEffect;

@SpellInfo (
		name="Reducto",
		description="descReducto",
		range=50,
		goThroughWalls=false,
		cooldown=300
)
public class Reducto extends Spell {
    
    public Reducto(HPS instance) {
        super(instance);
    }

    public boolean cast(Player p) {
    	HPS.SpellTargeter.register(p, new SpellHitEvent(){

			@Override
			public void hitBlock(Block block) {
				if(block.getType() != Material.AIR){
					block.getWorld().createExplosion(block.getLocation(), 4, false);
				}
			}

			@Override
			public void hitEntity(LivingEntity entity) {
				// TODO Auto-generated method stub
				
			}
    		
    	}, 1.0, ParticleEffect.EXPLODE);
		
		return true;
	}

}
