package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class Confringo extends Spell {
	
	public Confringo(HarryPotterSpells instance) {
		super(instance);
	}

	public void cast(Player p) {
		Fireball fb = p.launchProjectile(Fireball.class);
		fb.setShooter(p);
		fb.setYield(2);
		fb.setBounce(false);
	}
	
}
