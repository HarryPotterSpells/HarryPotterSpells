package com.hpspells.core.spell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.SpellTargeter.SpellHitEvent;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo(
        name = "Expelliarmus",
        description = "descExpelliarmus",
        range = 25,
        goThroughWalls = false,
        cooldown = 120
)
public class Expelliarmus extends Spell {

    public Expelliarmus(HarryPotterSpells instance) {
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
                //HPS.getConfig().getStringList("wand.types").stream().map(Material::matchMaterial).collect(Collectors.toList())
                List<Material> disarmItems = new ArrayList<Material>();
                if ((Boolean) getConfig("disarm-weapons", true))
                    disarmItems.addAll(Arrays.asList(Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD, Material.BOW, Material.CROSSBOW));

                ItemStack itemStack = entity.getEquipment().getItemInMainHand();
                if (disarmItems.contains(itemStack.getType()) || HPS.WandManager.isWand(itemStack)) {
                    Item i = entity.getWorld().dropItem(targetloc, entity.getEquipment().getItemInMainHand());
                    entity.getEquipment().setItemInMainHand(null);
                    Vector vector = targetloc.getDirection();
                    i.setVelocity(new Vector(vector.getX() * 2, vector.getY() * 2, vector.getZ() * 2));
                }

                return;
            }

        }, 1f, 0.5f, 10, Particle.DRIP_LAVA);
        return true;
    }
}
