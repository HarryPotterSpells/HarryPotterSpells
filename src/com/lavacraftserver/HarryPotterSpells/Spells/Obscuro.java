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
		description = "descObscuro", 
		range = 50, 
		goThroughWalls = false,
		cooldown=90
)
public class Obscuro extends Spell {

	public Obscuro(HPS plugin) {
        super(plugin);
    }

    @Override
	public boolean cast(Player p) {

		if (Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof LivingEntity) {
			LivingEntity le = Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			if (le instanceof Player) {

				/*
				 * TODO
				 * Not get the value from the config every spell cast;
				 * that's really resource intensive and inefficient
				 * 
				 * from Kezz101: well it's not really. Plus this method takes into account plugin/configuration reloading.
				 * Unless you want to create a ReloadJob and have every spell listen to it :/
				 * I guess that might be more resource intensive and innefficient
				 */
				int duration = (int) getTime("duration", 400);
				le.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 1));//blind them
				
				return true;
			} else if(le instanceof Creature) {
				
				((Creature) le).setTarget(null);//erase the creatures target
				le.setVelocity(new Vector(0,0,0));//stop them in their tracks
				
				// invert where they're looking (make them look in the opposite direction)
				Location loc = le.getLocation();
				loc.setPitch((loc.getPitch() + 90) % 180);
				loc.setYaw((loc.getYaw()+180) % 360);
				
				le.teleport(loc);
				
				return true;
			}
			
			return false;
		} else {
			HPS.PM.warn(p, HPS.Localisation.getTranslation("spellLivingEntityOnly"));
			return false;
		}
	}

}
