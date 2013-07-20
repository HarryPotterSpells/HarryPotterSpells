package com.hpspells.core.spell;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.ParticleEffect;

@SpellInfo (
	name="Aguamenti",
	description="descAguamenti",
	range=50,
	goThroughWalls=false,
	cooldown=90,
	icon=Material.WATER
	)
public class Aguamenti extends Spell {
    
    public Aguamenti(HPS instance) {
        super(instance);
    }

    public boolean cast(Player p) {
		HPS.SpellTargeter.register(p, new SpellHitEvent() {
			
			@Override
			public void hitEntity(LivingEntity entity) {
				hitBlock(entity.getEyeLocation().getBlock());
			}
			
			@Override
			public void hitBlock(final Block block) {
				if(block.getType().isTransparent()){
					block.setType(Material.WATER);
					Bukkit.getScheduler().scheduleSyncDelayedTask(HPS, new Runnable(){
						public void run(){
							block.setType(Material.AIR);
						}
					}, getTime("duration", 30));
				} else if(block.getRelative(BlockFace.UP).getType().isTransparent()){
					block.getRelative(BlockFace.UP).setType(Material.WATER);
					Bukkit.getScheduler().scheduleSyncDelayedTask(HPS, new Runnable(){
						public void run(){
							block.setType(Material.AIR);
						}
					}, getTime("duration", 30));
				}
			}
			
		}, 1.2f, ParticleEffect.DRIP_WATER);
		
		return true;
	}

}
