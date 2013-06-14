package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.SpellTargeter.SpellHitEvent;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.ParticleEffect;

@spell (
		name="Episkey",
		description="descEpiskey",
		range=50,
		goThroughWalls=false,
		cooldown=60
)
public class Episkey extends Spell {

	public boolean cast(final Player p) {
		HPS.SpellTargeter.register(p, new SpellHitEvent() {
			
			@Override
			public void hitBlock(Block block) {
				
				Block standingOn = block.getLocation().getBlock();
				Block pLocation = p.getLocation().subtract(0,1,0).getBlock();

				if (pLocation.equals(standingOn)) {
					int duration = (int) getTime("duration", 100);
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration, 1));
				} else
					HPS.PM.warn(p, HPS.Localisation.getTranslation("spellLivingEntityOrSelf"));
			}
			
			@Override
			public void hitEntity(LivingEntity entity) {
				int duration = (int) getTime("duration", 100);
				entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration, 1));
			}
			
		}, 1.05f, ParticleEffect.HEART);
		
		return true;
	}
	
}
