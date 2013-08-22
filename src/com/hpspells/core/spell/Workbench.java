package com.hpspells.core.spell;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;
import org.bukkit.entity.Player;

@SpellInfo(
        name = "Workbench",
        description = "descWorkbench",
        range = 50,
        goThroughWalls = false,
        cooldown = 20
)

public class Workbench extends Spell {

    public Workbench(HPS instance) {
        super(instance);
    }

    @Override
    public boolean cast(Player p) {
        p.openWorkbench(p.getLocation(), true);
        return true;
    }

}
