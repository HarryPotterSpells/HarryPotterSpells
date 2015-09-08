package com.hpspells.core.spell;

import com.hpspells.core.HPS;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpellInfo(
        name = "Expelliarmus",
        description = "descExpelliarmus",
        range = 25,
        goThroughWalls = false,
        cooldown = 120
)
public class Expelliarmus extends Spell {

    public Expelliarmus(HPS instance) {
        super(instance);
    }

    public boolean cast(final Player p) {
        HPS.SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                HPS.PM.warn(p, HPS.Localisation.getTranslation("spellLivingEntityOnly"));
                return;

            }

            @Override
            public void hitEntity(LivingEntity entity) {
                Location targetloc = entity.getLocation();
                List<Material> disarmItems = new ArrayList<Material>(Arrays.asList(Material.matchMaterial(HPS.getConfig().getString("wand.type"))));
                if ((Boolean) getConfig("disarm-weapons", true))
                    disarmItems.addAll(Arrays.asList(Material.STICK, Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLD_SWORD, Material.DIAMOND_SWORD, Material.BOW));

                if (disarmItems.contains(entity.getEquipment().getItemInHand().getType())) {
                    Item i = entity.getWorld().dropItem(targetloc, entity.getEquipment().getItemInHand());
                    entity.getEquipment().setItemInHand(null);
                    Vector vector = targetloc.getDirection();
                    i.setVelocity(new Vector(vector.getX() * 2, vector.getY() * 2, vector.getZ() * 2));
                }

                return;
            }

        }, 1f, 0.5f, 10, ParticleEffect.DRIP_LAVA);
        return true;
    }
}
