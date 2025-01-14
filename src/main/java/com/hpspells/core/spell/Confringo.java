package com.hpspells.core.spell;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.spell.Spell.SpellInfo;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

@SpellInfo(
        name = "Confringo",
        description = "descConfringo",
        range = 0,
        goThroughWalls = false,
        cooldown = 45
)
public class Confringo extends Spell {

    public Confringo(HarryPotterSpells instance) {
        super(instance);
    }

	public boolean cast(Player p) {
        Fireball fb = p.launchProjectile(Fireball.class);
        fb.setShooter(p);
        fb.setYield(2);
        //fb.setBounce(false);
        return true;
    }

}
