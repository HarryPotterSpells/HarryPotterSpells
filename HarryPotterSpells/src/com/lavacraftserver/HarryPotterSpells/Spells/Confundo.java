package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.Spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

public class Confundo {
	
	public static void cast(Player p) {
		SmallFireball sf = p.launchProjectile(SmallFireball.class);
		sf.setBounce(false);
		sf.setShooter(p);
		sf.setYield(0);
		Targeter.NeedsTargeted.put(sf.getUniqueId(), Spell.CONFUNDO);
	}
	
	public static void hit(Entity e) {
		if(e instanceof Player) {
			Player player = (Player)e;
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 1));
		}
	}
}
