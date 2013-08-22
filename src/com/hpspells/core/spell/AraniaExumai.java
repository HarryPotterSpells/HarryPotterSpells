package com.hpspells.core.spell;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.entity.*;

@SpellInfo(
        name = "Arania Exumai",
        description = "descAraniaExumai",
        range = 50,
        goThroughWalls = false,
        cooldown = 90
)
public class AraniaExumai extends Spell {

    public AraniaExumai(HPS instance) {
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
                //In case if one check doesn't work, check the other way as well
                if ((entity instanceof Spider) || (entity.getType() == EntityType.SPIDER) || (entity instanceof CaveSpider) || (entity.getType() == EntityType.CAVE_SPIDER)) {
                    // Do not cast Spider to entity because it can be a cave spider
                    entity.damage(6);
                    int knockback = (Integer) getConfig("knockback", 10);
                    entity.setVelocity(entity.getVelocity().add(entity.getLocation().toVector().subtract(p.getLocation().toVector().normalize().multiply(knockback))));
                }
            }

        }, 1.5, Effect.BLAZE_SHOOT);
        return true;
    }
}
