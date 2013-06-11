package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.SpellTargeter.SpellHitEvent;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.ParticleEffect;

@spell (
	name="Aguamenti",
	description="descAguamenti",
	range=50,
	goThroughWalls=false,
	cooldown=90,
	icon=Material.WATER
	)
public class Aguamenti extends Spell {
	
	public Aguamenti(HPS plugin) {
        super(plugin);
    }

    public boolean cast(Player p) {
		HPS.SpellTargeter.register(p, new SpellHitEvent() {
			
			@Override
			public void hitEntity(LivingEntity entity) {
				hitBlock(entity.getEyeLocation().getBlock());
			}
			
			@Override
			public void hitBlock(Block block) {
				if(block.getType().isTransparent())
					block.setType(Material.WATER);
				else if(block.getRelative(BlockFace.UP).getType().isTransparent())
					block.getRelative(BlockFace.UP).setType(Material.WATER);
			}
			
		}, 1.2f, ParticleEffect.DRIP_WATER);
		
		return true;
	}

}
