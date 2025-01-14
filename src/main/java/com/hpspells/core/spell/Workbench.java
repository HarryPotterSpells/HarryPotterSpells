package com.hpspells.core.spell;

import org.bukkit.entity.Player;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Workbench",
        description = "descWorkbench",
        range = 50,
        goThroughWalls = false,
        cooldown = 20
)

public class Workbench extends Spell {

    public Workbench(HarryPotterSpells instance) {
        super(instance);
    }

    @Override
    public boolean cast(Player p) {
        p.openWorkbench(p.getLocation(), true);
        return true;
    }

}
