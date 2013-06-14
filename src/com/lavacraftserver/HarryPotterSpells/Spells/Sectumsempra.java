package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Sectumsempra",
		description="descSectumsempra",
		range=50,
		goThroughWalls=false,
		cooldown=300
)
public class Sectumsempra extends Spell {

    @Override
	public boolean cast(Player p) {
		if (Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof LivingEntity) {
			LivingEntity le = Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			SectumsempraRunnable sectumsemprarunnable = new SectumsempraRunnable();
			sectumsemprarunnable.le = le;
			sectumsemprarunnable.taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(HPS.Plugin, sectumsemprarunnable, 0L, 20L);
			return true;
		} else {
			HPS.PM.warn(p, HPS.Localisation.getTranslation("spellLivingEntityOnly"));
			return false;
		}
	}

	private class SectumsempraRunnable implements Runnable {
		LivingEntity le;
		int taskID;
		Random random = new Random();
		int iterator = 0;
		final int length = random.nextInt(4) + 2;
		
		@Override
		public void run() {
			if (le.isValid())
				le.damage(1);
			else
			    Bukkit.getServer().getScheduler().cancelTask(taskID);

			if (iterator < length)
				iterator++;
			else
			    Bukkit.getServer().getScheduler().cancelTask(taskID);
		}

	}
}
