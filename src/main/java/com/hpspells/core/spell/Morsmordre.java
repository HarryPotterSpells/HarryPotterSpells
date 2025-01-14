package com.hpspells.core.spell;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.spell.Spell.SpellInfo;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

@SpellInfo(
        name = "Morsmordre",
        description = "descMorsmordre",
        cooldown = 60
)
public class Morsmordre extends Spell {

    public Morsmordre(HarryPotterSpells instance) {
        super(instance);
    }

    @Override
    public boolean cast(Player p) {
        Firework firework = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
        FireworkMeta fm = firework.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder().with(Type.CREEPER).withColor(Color.GREEN, Color.LIME, Color.GRAY).flicker(true).trail(true).withFade(Color.BLACK).build());
        fm.setPower(3);
        firework.setFireworkMeta(fm);
        return true;
    }

}
