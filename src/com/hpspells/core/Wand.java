package com.hpspells.core;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.hpspells.core.api.event.wand.WandCreationEvent;
import com.hpspells.core.spell.Spell;
import com.hpspells.core.util.MiscUtilities;

public class Wand extends ItemStack {
	
	private HPS HPS;
	
	private ItemStack itemstack;
	private Lore lore;
	private UUID owner;
	
	public Wand() {
		this(null);
	}
	
	public Wand(@Nullable Player owner) {
		this(owner, false);
	}
	
	public Wand(@Nullable Player owner, boolean loreless) {
		this.HPS = com.hpspells.core.HPS.getInstance();
		if (loreless) {
			makeLorelessWand();
		} else {
			makeWand(owner);
		}
	}
	
	/**
     * Gets the wand
     *
     * @param owner a {@link Nullable} parameter that specifies the owner of the wand
     *
     * @return an {@link ItemStack} that has been specified as a wand in the config
     */
    private void makeWand(@Nullable Player owner) {
        Material wandMaterial = Material.matchMaterial((String) getConfig("type", "STICK"));

        if (wandMaterial == null || wandMaterial == Material.AIR) {
            wandMaterial = Material.STICK;
            HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errWandCreationInvalidType", (String) getConfig("type", "STICK")));
        }

        itemstack = new ItemStack(wandMaterial);
        ItemMeta meta = HPS.getServer().getItemFactory().getItemMeta(wandMaterial);
        WandCreationEvent wandCreationEvent = new WandCreationEvent(owner, null, true);
        //NBTTagCompound comp = new NBTTagCompound(TAG_NAME);

        if ((Boolean) getConfig("lore.enabled", true)) {
        	Lore lore = generateLore();
        	if ((Boolean) getConfig("lore.show-current-spell", true)) {
            	Spell spell = HPS.SpellManager.getCurrentSpell(owner);
            	lore.setCurrentSpell((spell == null ? "None" : spell.getName()));
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

        meta.setDisplayName(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', (String) getConfig("lore.name", "Wand")));
        itemstack.setItemMeta(meta);

        if (wandCreationEvent.hasEnchantmentEffect()) {
            try {
                itemstack = MiscUtilities.makeGlow(itemstack);
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
    }
    
    /**
     * Gets a wand without lore.
     * Used for crafting so that people can't determine wand lore whilst crafting.
     *
     * @return an {@link ItemStack} containing a wand
     */
    public void makeLorelessWand() {
        Material wandMaterial = Material.matchMaterial((String) getConfig("type", "STICK"));

        if (wandMaterial == null || wandMaterial == Material.AIR) {
            wandMaterial = Material.STICK;
            HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errWandCreationInvalidType", (String) getConfig("type", "STICK")));
        }

        itemstack = new ItemStack(wandMaterial);
        ItemMeta meta = HPS.getServer().getItemFactory().getItemMeta(wandMaterial);

        meta.setDisplayName(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', (String) getConfig("lore.name", "Wand")));
        itemstack.setItemMeta(meta);

        if ((Boolean) getConfig("enchantment-effect", true)) {
            try {
            	itemstack = MiscUtilities.makeGlow(itemstack);
            } catch (Exception e) {
                HPS.PM.debug(HPS.Localisation.getTranslation("errEnchantmentEffect"));
                HPS.PM.debug(e);
            }
        }

    }
    
    private Object getConfig(String string, Object defaultt) {
        return HPS.getConfig().get("wand." + string, defaultt);
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
	
	public ItemStack getItemstack() {
		return itemstack;
	}

	public Lore getLore() {
		return lore;
	}

	public void setLore(Lore lore) {
		this.lore = lore;
		itemstack.getItemMeta().setLore(lore.toStringList());
	}
	
	public class Lore {
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
