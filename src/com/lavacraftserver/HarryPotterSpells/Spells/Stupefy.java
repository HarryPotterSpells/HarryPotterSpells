package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.SpellTargeter.SpellHitEvent;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Stupefy",
		description="descStupefy",
		range=50,
		goThroughWalls=false,
		cooldown=120
)
public class Stupefy extends Spell {

    @Override
	public boolean cast(final Player p) {
		HPS.SpellTargeter.register(p, new SpellHitEvent() {

			@Override
			public void hitBlock(Block block) {
				HPS.PM.warn(p, HPS.Localisation.getTranslation("spellLivingEntityOnly"));
			}

			@Override
			public void hitEntity(LivingEntity le) {
				le.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (int) getTime("confusion-duration", 200l), 1));
				le.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, (int) getTime("weakness-duration", 100l), 1));
				
				Vector unitVector = le.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
				le.setVelocity(unitVector.multiply((Integer) getConfig("vertical-knockback", 2)));
				le.setVelocity(le.getVelocity().setY((Double) getConfig("horizontal-knockback", 0.5d)));
				le.damage((Integer) getConfig("damage", 2));
			}
			
		}, 1.05, FireworkEffect.builder().trail(false).flicker(true).withColor(Color.RED).with(Type.BURST).build());
		
		return true;
	}
	
}