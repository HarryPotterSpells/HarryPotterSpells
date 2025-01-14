package com.hpspells.core.spell;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Magna Tonitrus",
        description = "descMagnaTonitrus",
        range = 50,
        goThroughWalls = false,
        cooldown = 60
)

public class MagnaTonitrus extends Spell {

    public MagnaTonitrus(HarryPotterSpells instance) {
        super(instance);
    }

    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                if (!block.getType().equals(Material.AIR) && !block.getType().equals(Material.CAVE_AIR) && !block.getType().equals(Material.VOID_AIR)) {
                    block.getWorld().strikeLightningEffect(block.getLocation());
                    Block above = new Location(block.getWorld(), block.getX(), block.getY() + 1, block.getZ()).getBlock();
                    if (above.getType().equals(Material.FIRE)) above.setType(Material.AIR);
                }
            }

            @Override
            public void hitEntity(LivingEntity entity) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellBlockOnly"));
            }

        }, 1.0, Effect.BLAZE_SHOOT);

        return true;
    }
}
