package com.hpspells.core.spell;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.ParticleEffect;
import com.hpspells.core.util.Targeter;

@SpellInfo (
		name="Alarte Ascendare",
		description="descAlarteAscendare",
		range=30,
		goThroughWalls=false,
		cooldown=45
)
public class AlarteAscendare extends Spell {

    public AlarteAscendare(HPS instance) {
        super(instance);
    }

    @Override
	public boolean cast(Player p) {
    	HPS.SpellTargeter.register(p, new SpellHitEvent() {

			@Override
			public void hitBlock(Block block) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void hitEntity(LivingEntity entity) {
				entity.setVelocity(new Vector(0,1,0));
			}
    		
    	}, 1.0, ParticleEffect.EXPLODE);
		return true;
	}

}
