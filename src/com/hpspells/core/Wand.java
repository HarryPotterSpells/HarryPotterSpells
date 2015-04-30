package com.hpspells.core;

import java.util.Arrays;
import java.util.Random;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.hpspells.core.util.MiscUtilities;

/**
 * This class manages the wand
 */
public class Wand {
    private HPS HPS;

    public static final String[] WOOD_TYPES = new String[]{"Elder", "Walnut", "Blackthorn", "Ash", "Hawthorn", "Rose", "Hornbeam", "Holly", "Vine", "Mahogany", "Willow", "Elm", "Oak", "Fir", "Cherry", "Chestnut", "Alder", "Yew"};
    public static final String[] CORES = new String[]{"Thestral tail hair", "Dragon heartstring", "Troll whisker", "Unicorn hair", "Veela hair", "Phoenix feather"};

    /**
     * Constructs a new {@link Wand}
     *
     * @param instance an instance of {@link HPS}
     */
    public Wand(HPS instance) {
        this.HPS = instance;
    }

    /**
     * Checks if a given {@link ItemStack} is useable as a wand
     *
     * @param i the itemstack
     * @return {@code true} if the ItemStack is useable as a wand
     */
    public boolean isWand(ItemStack i) {
    	 if (i.getTypeId() != ((Integer) getConfig("id", 280))) // Item id check
             return false;

         if (((Boolean) getConfig("lore.enabled", true)) && !i.getItemMeta().getDisplayName().equals((String) getConfig("lore.name", "Wand"))) // Lore name check
             return false;

         return true;
        //return new NBTContainerItem(i).getTag(TAG_NAME) != null;
    }

    /**
     * Gets the wand
     *
     * @param owner a {@link Nullable} parameter that specifies the owner of the wand
     *
     * @return an {@link ItemStack} that has been specified as a wand in the config
     */
    public ItemStack getWand(@Nullable Player owner) {
        ItemStack wand = new ItemStack((Integer) getConfig("id", 280));
        //NBTTagCompound comp = new NBTTagCompound(TAG_NAME);

        if ((Boolean) getConfig("lore.enabled", true)) {
            ItemMeta meta = wand.getItemMeta();
            Random random = new Random();

            meta.setDisplayName((String) getConfig("lore.name", "Wand"));
            meta.setLore(Arrays.asList(WOOD_TYPES[random.nextInt(WOOD_TYPES.length)] + " wood", CORES[random.nextInt(CORES.length)] + " core", random.nextInt(20) + " inches long"));

            wand.setItemMeta(meta);
        }

        if ((Boolean) getConfig("enchantment-effect", true)) {
            try {
                wand = MiscUtilities.makeGlow(wand);
            } catch (Exception e) {
                HPS.PM.debug(HPS.Localisation.getTranslation("errEnchantmentEffect"));
                HPS.PM.debug(e);
            }
        }

        /*if(owner != null) {
            NBTTagString tag = new NBTTagString();
            tag.setName("Owner");
            tag.set(owner.getName());

            comp.set("Owner", tag);
        }

        NBTContainerItem item = new NBTContainerItem(wand);
        item.setTag(comp);*/
        return wand;
    }

    /**
     * Gets a wand without lore.
     * Used for crafting so that people can't determine wand lore whilst crafting.
     *
     * @return an {@link ItemStack} containing a wand
     */
    public ItemStack getLorelessWand() {
        ItemStack wand = new ItemStack((Integer) getConfig("id", 280));

        if ((Boolean) getConfig("lore.enabled", true)) {
            ItemMeta meta = wand.getItemMeta();
            meta.setDisplayName((String) getConfig("lore.name", "Wand"));
            wand.setItemMeta(meta);
        }

        if ((Boolean) getConfig("enchantment-effect", true))
            try {
                wand = MiscUtilities.makeGlow(wand);
            } catch (Exception e) {
                HPS.PM.debug(HPS.Localisation.getTranslation("errEnchantmentEffect"));
                HPS.PM.debug(e);
            }

        return wand;
    }

    private Object getConfig(String string, Object defaultt) {
        return HPS.getConfig().get("wand." + string, defaultt);
    }

}
