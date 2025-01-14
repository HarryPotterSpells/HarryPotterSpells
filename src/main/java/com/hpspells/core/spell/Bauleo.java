package com.hpspells.core.spell;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.spell.Spell.SpellInfo;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@SpellInfo(
        name = "Bauleo",
        description = "descBauleo",
        range = 10,
        goThroughWalls = true,
        cooldown = 10
)
public class Bauleo extends Spell {

    public Bauleo(HarryPotterSpells instance) {
        super(instance);
    }

    @Override
    public boolean cast(Player p) {
        Inventory inventory = null;
        Location loc = p.getLocation();
        int radius = 5;
        outerLoop:
        for (int x = -(radius); x <= radius; x++) {
            for (int y = -(radius); y <= radius; y++) {
                for (int z = -(radius); z <= radius; z++) {
                    Block b = new Location(p.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z).getBlock();
                    if (b.getType() == Material.CHEST || b.getType() == Material.TRAPPED_CHEST || b.getType().name().contains("SHULKER_BOX")) {
                        //TODO: Find way to implement CHEST_MINECART (Entity) not block.
                        // Container is the parent class for Chest, ShulkerBox
                        inventory = ((Container) b.getState()).getInventory();
                        break outerLoop;
                    } else if (b.getType() == Material.ENDER_CHEST) {
                        inventory = p.getEnderChest();
                        break outerLoop;
                    }
                }
            }
        }
        if (inventory == null) {
            return false;
        }
        for (Entity e : p.getNearbyEntities(5, 5, 5)) {
            if (e instanceof Item) {
                Item item = (Item) e;
                // Only add item into inventory if it is not full
                if (inventory.firstEmpty() != -1) {
                    inventory.addItem(item.getItemStack());
                    item.remove();
                }
            }
        }
        return true;
    }

}
