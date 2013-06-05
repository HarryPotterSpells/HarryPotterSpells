package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.ParticleEffect;
import com.lavacraftserver.HarryPotterSpells.Utils.SpellTargeter;
import com.lavacraftserver.HarryPotterSpells.Utils.SpellTargeter.SpellHitEvent;

@spell (
		name="Episkey",
		description="Slowly heals your target",
		range=50,
		goThroughWalls=false,
		cooldown=60
)
public class Episkey extends Spell {

	public boolean cast(final Player p) {
		SpellTargeter.register(p, new SpellHitEvent() {
			
			@Override
			public void hitBlock(Block block) {
				
				HPS.PM.warn(p, "This can only be used on a player or a mob.");
			}
			
			@Override
			public void hitEntity(LivingEntity entity) {
				
				String durationString = HPS.Plugin.getConfig().getString("spells.episkey.duration", "100t");
				int duration = 0;
				
				if (durationString.endsWith("t")) {
					String ticks = durationString.substring(0, durationString.length() - 1);
					duration = Integer.parseInt(ticks);
				} else {
					duration = Integer.parseInt(durationString) * 20;
				}
				
				entity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, duration, 1));
			}
			
		}, 1.05f, ParticleEffect.HEART);
		
		return true;
	}
	
}
