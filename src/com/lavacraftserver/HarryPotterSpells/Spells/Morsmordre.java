package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import com.lavacraftserver.HarryPotterSpells.Spells.Spell.SpellInfo;

@SpellInfo(
        name = "Morsmordre",
        description = "descMorsmordre",
        cooldown = 60
        )
public class Morsmordre extends Spell {

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
