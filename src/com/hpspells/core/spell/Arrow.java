package com.hpspells.core.spell;

import org.bukkit.entity.Player;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Arrow",
        description = "descArrow",
        goThroughWalls = false,
        cooldown = 90
)
public class Arrow extends Spell {

    public Arrow(HPS instance) {
        super(instance);
    }

    @Override
    public boolean cast(Player p) {
        Double velocityModifier = (Double) getConfig("velocity", 1.2);
        p.launchProjectile(org.bukkit.entity.Arrow.class, p.getLocation().getDirection().multiply(velocityModifier));
        return true;
    }

}
