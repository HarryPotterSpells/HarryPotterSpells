package com.hpspells.core;

import java.util.*;

import javax.annotation.Nullable;

import com.hpspells.core.api.event.wand.WandCreationEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.hpspells.core.util.MiscUtilities;

/**
 * This class manages the wand
 */
public class Wand {
    private HPS HPS;

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
    	 if (i.getTypeId() != (Integer) getConfig("id", 280)) // Item id check
             return false;

         if ((Boolean) getConfig("lore.enabled", true) && !i.getItemMeta().getDisplayName().equals((String) getConfig("lore.name", "Wand"))) // Lore name check
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
        ItemMeta meta = wand.getItemMeta();
        WandCreationEvent wandCreationEvent = new WandCreationEvent(owner, null, true);
        //NBTTagCompound comp = new NBTTagCompound(TAG_NAME);

        if ((Boolean) getConfig("lore.enabled", true)) {
            wandCreationEvent.setLore(generateLore());
        }

        if ((Boolean) getConfig("enchantment-effect", true)) {
            wandCreationEvent.setEnchantmentEffect(true);
        }

        // Call creation event
        HPS.getServer().getPluginManager().callEvent(wandCreationEvent);

        if (wandCreationEvent.hasEnchantmentEffect()) {
            try {
                wand = MiscUtilities.makeGlow(wand);
            } catch (Exception e) {
                HPS.PM.debug(HPS.Localisation.getTranslation("errEnchantmentEffect"));
                HPS.PM.debug(e);
            }
        }

        if (wandCreationEvent.hasLore()) {
            Lore lore = wandCreationEvent.getLore();

            meta.setLore(Arrays.asList(lore.getLength() + " " + getConfig("lore.length.measurement", "inches"),
                    lore.getCore() + " core",
                    lore.getWood() + " wood (" + lore.getWoodRarityString() + ")",
                    lore.getRigidity(),
                    "Manufactured by " + lore.getManufacturer()));
        }

        meta.setDisplayName((String) getConfig("lore.name", "Wand"));
        wand.setItemMeta(meta);

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
        ItemMeta meta = wand.getItemMeta();

        meta.setDisplayName((String) getConfig("lore.name", "Wand"));
        wand.setItemMeta(meta);

        if ((Boolean) getConfig("enchantment-effect", true))
            try {
                wand = MiscUtilities.makeGlow(wand);
            } catch (Exception e) {
                HPS.PM.debug(HPS.Localisation.getTranslation("errEnchantmentEffect"));
                HPS.PM.debug(e);
            }

        return wand;
    }

    public Lore generateLore() {
        Lore lore = new Lore();
        Random random = new Random();
        ConfigurationSection cs = HPS.getConfig().getConfigurationSection("wand.lore");

        int woodRarity = MiscUtilities.randomBetween(1, 6);
        List<String> possibleRarities = cs.getStringList("wood-types." + woodRarity),
                possibleRigidities = cs.getStringList("rigidity");

        Map<String, Object> cores = cs.getConfigurationSection("cores").getValues(false),
                manufacturers = cs.getConfigurationSection("manufacturers").getValues(false);

        lore.setWoodRarity(woodRarity);
        lore.setWood(possibleRarities.get(random.nextInt(possibleRarities.size())));
        lore.setRigidity(possibleRigidities.get(random.nextInt(possibleRigidities.size())));
        lore.setLength(MiscUtilities.randomBetween(cs.getInt("length.minimum", 9), cs.getInt("length.maximum", 18)));
        lore.setCore(MiscUtilities.getStringFromProbability(cores));
        lore.setManufacturer(MiscUtilities.getStringFromProbability(manufacturers));

        return lore;
    }

    private Object getConfig(String string, Object defaultt) {
        return HPS.getConfig().get("wand." + string, defaultt);
    }

    public class Lore {
        private String wood = "Unknown", manufacturer = "somebody", core = "Unknown", rigidity = "Unknown rigidity";
        private int woodRarity = 0, length = 13;

        public String getWood() {
            return wood;
        }

        public void setWood(String wood) {
            this.wood = wood;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getCore() {
            return core;
        }

        public void setCore(String core) {
            this.core = core;
        }

        public String getRigidity() {
            return rigidity;
        }

        public void setRigidity(String rigidity) {
            this.rigidity = rigidity;
        }

        public void setWoodRarity(int woodRarity) {
            this.woodRarity = woodRarity;
        }

        public int getWoodRarity() {
            return woodRarity;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public String getWoodRarityString() {
            switch (woodRarity) {
                case 1: return "very rare";
                case 2: return "rare";
                case 3: return "very uncommon";
                case 4: return "uncommon";
                case 5: return "common";
                default: return "unknown rarity";
            }
        }

    }

}
