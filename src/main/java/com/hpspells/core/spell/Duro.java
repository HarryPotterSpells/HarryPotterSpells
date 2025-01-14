package com.hpspells.core.spell;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.HPSParticle;

@SpellInfo(
        name = "Duro",
        description = "descDuro",
        range = 50,
        goThroughWalls = false,
        cooldown = 90,
        icon = Material.STONE
)
public class Duro extends Spell {

    public Duro(HarryPotterSpells instance) {
        super(instance);
    }

    @Override
    public boolean cast(Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {
            
            @Override
            public void hitEntity(LivingEntity entity) {
                hitBlock(entity.getEyeLocation().getBlock());
            }

            @Override
            public void hitBlock(Block block) {
                block.setType(Material.STONE);
            }

        }, 1, 0.5, 2, new HPSParticle(Particle.REDSTONE, new DustOptions(Color.GRAY, 1)));
        return false;
    }

}
