package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

import com.lavacraftserver.HarryPotterSpells.PM;
import com.lavacraftserver.HarryPotterSpells.Spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;


public class AvadaKedavra {

	public static void cast(Player p) {
		SmallFireball sf = p.launchProjectile(SmallFireball.class);
		sf.setBounce(false);
		sf.setShooter(p);
		sf.setYield(0);
		Targeter.NeedsTargeted.put(sf.getUniqueId(), Spell.AVADAKEDAVRA);
	}
	
	//Change soon!
	static Player caster;
	
	public static void hit(Entity e) {
		if(e instanceof LivingEntity) {
			LivingEntity livingentity = (LivingEntity)e;
			livingentity.setHealth(0);
			if (PM.hps.getConfig().getBoolean("shout-on-spell-use") == true) {
				caster.chat("Avada Kedavra!");
			}
			if (PM.hps.getConfig().getBoolean("notify-on-spell-use") == true) {
				caster.sendMessage(ChatColor.AQUA + "You have cast Avada Kedavra!");
			}
		} else {
			caster.sendMessage(ChatColor.RED + "You must be targetting a player or mob!");
		}
	}
}
