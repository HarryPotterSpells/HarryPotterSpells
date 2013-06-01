package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell(
		name = "Obscuro", 
		description = "Blinds the target mob or player", 
		range = 50, 
		goThroughWalls = false,
		cooldown=60
)
public class Obscuro extends Spell {

	@Override
	public boolean cast(Player p) {

		if (Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof LivingEntity) { //if a LivingEntity is targeted by the spell
			LivingEntity le = Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			if (le instanceof Player) {//if the targeted is a player

				/*
				 * TODO
				 * Not get the value from the config every spell cast;
				 * that's really resource intensive and inefficient
				 */
				String durationString = HPS.Plugin.getConfig().getString("spells.obscuro.duration");
				int duration = 0;
				
				if (durationString.endsWith("t")) {
					String ticks = durationString.substring(0, durationString.length() - 1);
					duration = Integer.parseInt(ticks);
				} else {
					duration = Integer.parseInt(durationString) * 20;
				}
				
				le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 1));//blind them

			} else if(le instanceof Creature) {//else if it's a creature that's not a player
				
				((Creature) le).setTarget(null);//erase the creatures target
				le.setVelocity(new Vector(0,0,0));//stop them in their tracks
				// invert where they're looking (make them look in the opposite direction)
				Location loc = le.getLocation();
				loc.setPitch((loc.getPitch() + 90) % 180);
				loc.setYaw((loc.getYaw()+180) % 360);
				//put the changes in effect
				le.teleport(loc);
				
			}
			
			return true;
			
		} else {
			
			HPS.PM.warn(p, "This can only be used on a player or a mob.");
			return false;
			
		}
	}

}
