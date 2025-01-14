package com.hpspells.core.spell;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.spell.Spell.SpellInfo;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SpellInfo(
        name = "Flame Freezing Charm",
        description = "descFlameFreezingCharm",
        range = 50,
        goThroughWalls = false,
        cooldown = 300
)

public class FlameFreezingCharm extends Spell {

    public FlameFreezingCharm(HarryPotterSpells instance) {
        super(instance);
    }

    @Override
    public boolean cast(Player p) {
        int duration = (int) getTime("duration", 60 * 20);
        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, duration, 1));
        HPS.PM.tell(p, "You are now Fire Resistant for " + duration / 20 + " seconds.");
        return true;
    }


}
