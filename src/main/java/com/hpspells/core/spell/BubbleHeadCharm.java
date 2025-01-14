package com.hpspells.core.spell;

import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Bubble Head Charm",
        description = "descBubbleHeadCharm",
        range = 50,
        goThroughWalls = false,
        cooldown = 45
)

public class BubbleHeadCharm extends Spell {


    public BubbleHeadCharm(HarryPotterSpells instance) {
        super(instance);
    }

    @Override
    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellLivingEntityOnly"));
            }

			@Override
            public void hitEntity(LivingEntity entity) {
                if (entity instanceof Player) {
                    Player player = (Player) entity;
                    //Gets block above player and bellow player
                    /*Block feet = player.getLocation().getBlock();
                    Block head = player.getLocation().add(0, 1, 0).getBlock();*/

                    //Checks if it matches water to make sure they are underwater
                    //TODO: Check water level to see if its stationary water 1.13
//                    if ((feet.getType() == Material.WATER) && (head.getType() == Material.WATER)) {
//                        //Sets player air in ticks (Default max = 300)
//                        player.setRemainingAir(300);
//                    }
                    player.setRemainingAir(300);
                }
            }

        }, 1f, Particle.WATER_WAKE);
        return true;
    }

}
