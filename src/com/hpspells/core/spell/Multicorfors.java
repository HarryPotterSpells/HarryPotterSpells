package com.hpspells.core.spell;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.material.Colorable;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.ParticleEffect;

@SpellInfo(
        name = "Multicorfors",
        description = "descMulticorfors",
        range = 25,
        goThroughWalls = false,
        cooldown = 30
)
public class Multicorfors extends Spell {

    public Multicorfors(HPS instance) {
        super(instance);
    }

    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(final Block block) { // Hit wool
                if (block.getState().getData() instanceof Colorable) {

                    if ((Boolean) getConfig("explosionEffect", true))
                        block.getWorld().createExplosion(block.getLocation(), 0F);

                    Bukkit.getScheduler().runTask(HPS, new Runnable() {

                        @Override
                        public void run() {
                            BlockState blockState = block.getState();
                            Colorable colorable = (Colorable) blockState.getData();
                            colorable.setColor(randomDyeColor());
                            blockState.update();
                        }

                    });
                } else {
                    HPS.PM.warn(p, "You cannot use this spell on that block.");
                }
            }

            @Override
            public void hitEntity(LivingEntity entity) { // Hit sheep
                if (entity.getType() == EntityType.SHEEP) {
                    final Sheep sheep = (Sheep) entity;

                    if ((Boolean) getConfig("explosionEffect", true))
                        sheep.getWorld().createExplosion(sheep.getLocation(), 0F);

                    Bukkit.getScheduler().runTask(HPS, new Runnable() {

                        @Override
                        public void run() {
                            sheep.setColor(randomDyeColor());
                        }

                    });
                }
            }

        }, 1.1f, ParticleEffect.REDSTONE);

        return true;
    }

    private DyeColor randomDyeColor() {
        DyeColor et;
        int maxMobs = 16, minMobs = 1;
        int randomNum = new Random().nextInt(maxMobs - minMobs + 1) + minMobs;

        switch (randomNum) {
            case 1:
                et = DyeColor.BLACK;
                break;
            case 2:
                et = DyeColor.BLUE;
                break;
            case 3:
                et = DyeColor.BROWN;
                break;
            case 4:
                et = DyeColor.CYAN;
                break;
            case 5:
                et = DyeColor.GRAY;
                break;
            case 6:
                et = DyeColor.GREEN;
                break;
            case 7:
                et = DyeColor.LIGHT_BLUE;
                break;
            case 8:
                et = DyeColor.LIME;
                break;
            case 9:
                et = DyeColor.MAGENTA;
                break;
            case 10:
                et = DyeColor.ORANGE;
                break;
            case 11:
                et = DyeColor.PINK;
                break;
            case 12:
                et = DyeColor.PURPLE;
                break;
            case 13:
                et = DyeColor.RED;
                break;
            case 14:
                et = DyeColor.LIGHT_GRAY;
                break;
            case 15:
                et = DyeColor.WHITE;
                break;
            case 16:
                et = DyeColor.YELLOW;
                break;
            default:
                et = DyeColor.WHITE;
                break;
        }
        return et;
    }

}
