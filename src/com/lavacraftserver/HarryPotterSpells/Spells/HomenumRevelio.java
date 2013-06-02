package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.logging.Level;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.FireworkEffectPlayer;

@spell (
        name = "Homenum Revelio",
        description = "Reveals players nearby the caster",
        cooldown = 600
        )
public class HomenumRevelio extends Spell {

    @Override
    public boolean cast(Player p) {
        ConfigurationSection section = HPS.Plugin.getConfig().getConfigurationSection("homenum-revelio");
        for(Entity entity : p.getNearbyEntities(section.getDouble("box.x", 10d), section.getDouble("box.y", 10d), section.getDouble("box.z", 10d))) {
            if(entity instanceof Player) {
                try {
                    FireworkEffectPlayer.playFirework(entity.getWorld(), entity.getLocation(), FireworkEffect.builder().flicker(false).with(Type.BURST).withColor(Color.YELLOW).build());
                } catch (Exception e) {
                    HPS.PM.log(Level.WARNING, "An error occurred whilst playing a firework effect.");
                    HPS.PM.debug(e);
                }
            }
        }
        return true;
    }

}
