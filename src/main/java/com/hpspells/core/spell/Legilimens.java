package com.hpspells.core.spell;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Legilimens",
        description = "descLegilimens",
        range = 50,
        goThroughWalls = false,
        cooldown = 180
)
public class Legilimens extends Spell {

    public Legilimens(HarryPotterSpells instance) {
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
                    Player tarPlayer = (Player) entity;
                    //Get Target Players inventory
                    Inventory inv = tarPlayer.getInventory();
                    //Get time
                    long duration = getTime("duration", 20);
                    //Open Target inventory on own screen
                    p.openInventory(inv);
                    HPS.PM.tell(p, "You are now viewing " + tarPlayer.getName() + "'s inventory for " + duration + " seconds!");
                    //Cooldown before inventory force close
                    Bukkit.getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {
                        public void run() {
                            p.closeInventory();
                        }
                    }, duration);
                }
            }

        }, 1.00, Effect.SMOKE);
        return true;
    }


}
