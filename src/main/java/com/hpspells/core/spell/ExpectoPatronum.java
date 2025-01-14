package com.hpspells.core.spell;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.HPSParticle;

@SpellInfo(
        name = "Expecto Patronum",
        description = "descExpectoPatronum",
        range = 50,
        goThroughWalls = false,
        cooldown = 45
)
public class ExpectoPatronum extends Spell {

    public ExpectoPatronum(HarryPotterSpells instance) {
        super(instance);
    }
    
    private List<EntityType> allowedEntities = Arrays.asList(EntityType.PHANTOM,
    		EntityType.VEX,
    		EntityType.WARDEN,
    		EntityType.GHAST);
    
    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellLivingEntityOnly"));
            }

            @Override
            public void hitEntity(LivingEntity entity) {
                if (checkEntity(entity.getType())) {
                    Integer damage = (Integer) getConfig("damage", 5);
                    Double velocity = handleDouble(getConfig("velocity", 3.0), 3.0);
                    entity.damage(damage);
                    entity.setVelocity(p.getPlayer().getLocation().getDirection().multiply(velocity));
                }
            }

        }, 1, 0.5, 2, new HPSParticle(Particle.CLOUD, new DustOptions(Color.WHITE, 1)));
        return true;
    }
    
    private boolean checkEntity(EntityType e) {
    	for(EntityType et : allowedEntities) if(e.equals(et)) return true;
    	return false;
    }
}
