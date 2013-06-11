package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.logging.Level;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.FireworkEffectPlayer;

@spell (
        name = "Homenum Revelio",
        description = "descHomenumRevelio",
        cooldown = 600
        )
public class HomenumRevelio extends Spell {

    public HomenumRevelio(HPS plugin) {
        super(plugin);
    }

    @Override
    public boolean cast(Player p) {
        for(Entity entity : p.getNearbyEntities((Double) getConfig("box.x", 10d), (Double) getConfig("box.y", 10d), (Double) getConfig("box.z", 10d))) {
            if(entity instanceof Player) {
                try {
                    FireworkEffectPlayer.playFirework(entity.getWorld(), entity.getLocation(), FireworkEffect.builder().flicker(false).with(Type.BURST).withColor(Color.YELLOW).build());
                } catch (Exception e) {
                    HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errFireworkEffect"));
                    HPS.PM.debug(e);
                }
            }
        }
        return true;
    }

}
