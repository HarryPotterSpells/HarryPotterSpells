package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.Random;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Sectumsempra",
		description="Slowly damages the target player",
		range=50,
		goThroughWalls=false
)
public class Sectumsempra extends Spell {

	public Sectumsempra(HarryPotterSpells instance) {
		super(instance);
	}

	@Override
	public void cast(Player p) {
		if (Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof LivingEntity) {
			LivingEntity le = Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			SectumsempraRunnable sectumsemprarunnable = new SectumsempraRunnable();
			sectumsemprarunnable.le = le;
			sectumsemprarunnable.taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, sectumsemprarunnable, 0L, 20L);
		} else {
			plugin.PM.warn(p, "This may only be used on a player or a mob.");
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
			if (le.isValid()) {
				le.damage(1);
			} else {
				cancelTask();
			}
			if (iterator < length)
				iterator++;
			else
				cancelTask();
		}
		
		public void cancelTask() {
			plugin.getServer().getScheduler().cancelTask(taskID);
		}

	}
}
