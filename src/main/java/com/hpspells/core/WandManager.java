package com.hpspells.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.hpspells.core.api.event.wand.WandCreationEvent;
import com.hpspells.core.spell.Spell;
import com.hpspells.core.util.MiscUtilities;

/**
 * This class manages the wand
 */
public class WandManager {
    private HarryPotterSpells HPS;
    private List<Material> wandTypes = new ArrayList<Material>();

    /**
     * Constructs a new {@link WandManager}
     *
     * @param instance an instance of {@link HarryPotterSpells}
     */
    public WandManager(HarryPotterSpells instance) {
        this.HPS = instance;
        HPS.getConfig().getStringList("wand.types").forEach(string -> {
            Material material = Material.matchMaterial(string);
            if (material != null && material != Material.AIR) wandTypes.add(material);
        });
        if (wandTypes.isEmpty()) {
            wandTypes.add(Material.STICK);
        }
        HPS.PM.log(Level.INFO, "Registered " + wandTypes.size() + " wand materials!");
    }

    /**
     * Checks if a given {@link ItemStack} is useable as a wand
     *
     * @param i the itemstack
     * @return {@code true} if the ItemStack is useable as a wand
     */
    public boolean isWand(ItemStack i) {
        if (i == null || !i.hasItemMeta() || !wandTypes.contains(i.getType())) {
        	return false;
        }

        ItemMeta itemMeta = i.getItemMeta();

//        HPS.PM.broadcastMessage("Wand name: \n" + itemMeta.getDisplayName());
//        HPS.PM.broadcastMessage("No Color wand name: \n" + ChatColor.stripColor(itemMeta.getDisplayName()));
//        HPS.PM.broadcastMessage("No Color config: \n" + ChatColor.stripColor((String) getConfig("lore.name", "Wand")) );
//        HPS.PM.broadcastMessage("Matching?:" + ChatColor.stripColor(itemMeta.getDisplayName()).equals(ChatColor.stripColor(getName())));
        return itemMeta.hasDisplayName() && ChatColor.stripColor(itemMeta.getDisplayName()).equals(ChatColor.stripColor(getName()));
//        return new NBTContainerItem(i).getTag(TAG_NAME) != null;
    }

    public ItemStack getWand(@Nullable Player owner) {
        Material wandMaterial = wandTypes.get(0);
        return getWand(owner, wandMaterial);
    }
    
    /**
     * Gets the wand
     *
     * @param owner a {@link Nullable} parameter that specifies the owner of the wand
     *
     * @return an {@link ItemStack} that has been specified as a wand in the config
     */
    public ItemStack getWand(@Nullable Player owner, Material wandMaterial) {
        if (wandMaterial == null || wandMaterial == Material.AIR) {
            wandMaterial = wandTypes.get(0);
        }
        if (!wandTypes.contains(wandMaterial)) {
            return null;
        }
        ItemStack wand = new ItemStack(wandMaterial);
        ItemMeta meta = HPS.getServer().getItemFactory().getItemMeta(wandMaterial);
        WandCreationEvent wandCreationEvent = new WandCreationEvent(owner, null, true);
        //NBTTagCompound comp = new NBTTagCompound(TAG_NAME);

        if ((Boolean) getConfig("lore.enabled", true)) {
        	WandLore lore = generateLore();
        	if ((Boolean) getConfig("lore.show-current-spell", true)) {
            	Spell spell = HPS.SpellManager.getCurrentSpell(owner);
            	lore.setCurrentSpell(spell == null ? "None" : spell.getName());
            }
            wandCreationEvent.setLore(lore);
        }
        
        if ((Boolean) getConfig("enchantment-effect", true)) {
            wandCreationEvent.setEnchantmentEffect(true);
        }

        // Call creation event
        HPS.getServer().getPluginManager().callEvent(wandCreationEvent);

        if (wandCreationEvent.hasLore()) {
            meta.setLore(wandCreationEvent.getLore().toStringList());
        }

        meta.setDisplayName(ChatColor.RESET + getName());

        if (wandCreationEvent.hasEnchantmentEffect()) {
            try {
//                wand = MiscUtilities.makeGlow(wand);
                meta.addEnchant(Enchantment.LURE, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } catch (Exception e) {
                HPS.PM.debug(HPS.Localisation.getTranslation("errEnchantmentEffect"));
                HPS.PM.debug(e);
            }
        }
        
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
     * Gets a wand from any inventory, can be null if none found.
     * @param inv The inventory to search for
     * @return the wand ItemStack
     */
    public ItemStack getWandFromInventory(Inventory inv) {
    	for (ItemStack is : inv.getContents()) {
    		if (isWand(is)) {
    			return is;
    		}
    	}
    	return null;
    }

    /**
     * Gets a wand without lore.
     * Used for crafting so that people can't determine wand lore whilst crafting.
     *
     * @return an {@link ItemStack} containing a wand
     */
    public ItemStack getLorelessWand() {
        Material wandMaterial = wandTypes.get(0);

        if (wandMaterial == null || wandMaterial == Material.AIR) {
            wandMaterial = Material.STICK;
            HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errWandCreationInvalidType", Material.STICK));
        }

        ItemStack wand = new ItemStack(wandMaterial);
        ItemMeta meta = HPS.getServer().getItemFactory().getItemMeta(wandMaterial);

        meta.setDisplayName(ChatColor.RESET + getName());

        if ((Boolean) getConfig("enchantment-effect", true)) {
            try {
//                wand = MiscUtilities.makeGlow(wand);
                meta.addEnchant(Enchantment.LURE, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            } catch (Exception e) {
                HPS.PM.debug(HPS.Localisation.getTranslation("errEnchantmentEffect"));
                HPS.PM.debug(e);
            }
        }
        
        wand.setItemMeta(meta);

        return wand;
    }
    
    /**
     * Gets the avaliable wand types.
     * 
     * @return formatted wand name
     */
    public List<Material> getWandTypes() {
        return wandTypes;
    }
    
    /**
     * Gets the formatted wand name from the config.
     * 
     * @return formatted wand name
     */
    public String getName() {
    	return ChatColor.translateAlternateColorCodes('&', (String) getConfig("lore.name", "Wand"));
    }

    public WandLore generateLore() {
        WandLore lore = new WandLore();
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

    public class WandLore {
        private String currentSpell = "None", wood = "Unknown", manufacturer = "somebody", core = "Unknown", rigidity = "Unknown rigidity";
        private int woodRarity = 0, length = 13;

        public String getCurrentSpell() {
        	return currentSpell;
        }
        
        public void setCurrentSpell(String spellname) {
        	currentSpell = spellname;
        }
        
        public String getWood() {
            return wood;
        }

        public void setWood(String wood) {
            this.wood = wood;
        }

        public String getManufacturer() {
            return manufacturer.replace('_', ' ').trim();
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getCore() {
            return core.replace('_', ' ').trim();
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

        public List<String> toStringList() {
            List<String> formatList = HPS.getConfig().getStringList("wand.lore.format");

            for (int i = 0; i < formatList.size(); i++) {
                String line = formatList.get(i);

                line = line.replace("%spell", this.getCurrentSpell());
                line = line.replace("%length", String.valueOf(this.getLength()));
                line = line.replace("%core", this.getCore());
                line = line.replace("%wood", this.getWood());
                line = line.replace("%rarity", this.getWoodRarityString());
                line = line.replace("%rigidity", this.getRigidity());
                line = line.replace("%manufacturer", this.getManufacturer());
                line = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', line);

                formatList.set(i, line);
            }

            return formatList;
        }
    }

}
