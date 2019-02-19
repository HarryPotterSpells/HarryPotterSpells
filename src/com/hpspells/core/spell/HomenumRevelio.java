package com.hpspells.core.spell;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.FireworkEffectPlayer;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.logging.Level;

@SpellInfo(
        name = "Homenum Revelio",
        description = "descHomenumRevelio",
        cooldown = 600
)
public class HomenumRevelio extends Spell {

    public HomenumRevelio(HPS instance) {
        super(instance);
    }

    @Override
    public boolean cast(Player p) {
    	double x = ((Integer) getConfig("box.x", 10d)).doubleValue();
    	double y = ((Integer) getConfig("box.y", 10d)).doubleValue();
    	double z = ((Integer) getConfig("box.z", 10d)).doubleValue();
        for (Entity entity : p.getNearbyEntities(x, y, z)) {
            if (entity instanceof Player) {
                try {
                	FireworkEffect effect = FireworkEffect.builder().flicker(false).with(Type.BURST).withColor(Color.YELLOW).build();
                	FireworkEffectPlayer.playFirework(entity.getLocation(), effect);
//                	FireworkEffectPlayer.playFirework(entity.getWorld(), entity.getLocation(), FireworkEffect.builder().flicker(false).with(Type.BURST).withColor(Color.YELLOW).build());
                } catch (Exception e) {
                    HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errFireworkEffect"));
                    HPS.PM.debug(e);
                }
            }
        }
        return true;
    }

}
