package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lavacraftserver.HarryPotterSpells.Spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

public class Confundo {
	
	public static void cast(Player p) {
		Snowball snowball = p.launchProjectile(Snowball.class);
		snowball.setBounce(false);
		snowball.setShooter(p);
		enderPearlEffect(snowball);
		Targeter.NeedsTargeted.put(snowball.getUniqueId(), Spell.CONFUNDO);
	}
	
	public static void hit(Entity e, Snowball s) {
		if(e instanceof Player) {
			Targeter.NeedsTargeted.remove(s.getUniqueId());
			Player player = (Player)e;
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 1));
		}
	}
	
	public static void enderPearlEffect(Snowball s) {
		while(Targeter.NeedsTargeted.containsKey(s.getUniqueId())) {
			s.getWorld().playEffect(s.getLocation(), Effect.ENDER_SIGNAL, 0);
		}
	}
	
}
