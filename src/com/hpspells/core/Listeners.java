package com.hpspells.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.hpspells.core.api.event.SpellPreCastEvent;
import com.hpspells.core.configuration.ConfigurationManager.ConfigurationType;
import com.hpspells.core.configuration.PlayerSpellConfig;
import com.hpspells.core.spell.Colloportus;
import com.hpspells.core.spell.Spell;

public class Listeners implements Listener {
    private HPS HPS;
    
    private List<Material> buttonTypeList = new ArrayList<>(Arrays.asList(
    		Material.ACACIA_BUTTON,
    		Material.BAMBOO_BUTTON,
    		Material.BIRCH_BUTTON,
    		Material.CHERRY_BUTTON,
    		Material.CRIMSON_BUTTON,
    		Material.DARK_OAK_BUTTON,
    		Material.JUNGLE_BUTTON,
    		Material.MANGROVE_BUTTON,
    		Material.OAK_BUTTON,
    		Material.POLISHED_BLACKSTONE_BUTTON,
    		Material.SPRUCE_BUTTON,
    		Material.STONE_BUTTON,
    		Material.WARPED_BUTTON
    ));

    public static final Permission CAST_SPELLS = new Permission("harrypotterspells.cast", PermissionDefault.OP);

    public Listeners(HPS instance) {
        this.HPS = instance;
        HPS.getServer().getPluginManager().addPermission(CAST_SPELLS);
    }
    
    @EventHandler
    public void PIE(PlayerInteractEvent e) {
    	for(String world : HPS.getConfig().getStringList("disabledWorlds")) if(e.getPlayer().getWorld().getName().toLowerCase().equals(world.toLowerCase())) return;
        HPS.PM.debug("Triggered Once"); // Spellswitch triggered twice when right clicking on a block
        // MC 1.9 - Offhand introduction. Only listen to main hand events.
        if (e.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }
        
        //Below code to stop doors from opening for Colloportus
        if (e.getClickedBlock() != null) {
            Material material = e.getClickedBlock().getType();
            if (Colloportus.getDoorTypes().contains(material)) {
                if (Colloportus.isLockedDoor(e.getClickedBlock()) && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    HPS.PM.warn(e.getPlayer(), "This door has been locked by a Colloportus spell");
                    e.setCancelled(true);
                }
            }
            if (e.getAction().equals(Action.PHYSICAL)) {
                if (Colloportus.getPadTypes().contains(material)) {
//                    System.out.println("Pressure pad detected");
                }
            }
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (buttonTypeList.contains(material)) {
//                	System.out.println("Button press detected");
                }
            }
        }
        
        //Normal wand checking code
        if (e.getPlayer().hasPermission(CAST_SPELLS) && HPS.WandManager.isWand(e.getPlayer().getInventory().getItemInMainHand())) {
            PlayerSpellConfig psc = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);

            if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
                e.setCancelled(true);

            //Change spell
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Integer knows = psc.getStringListOrEmpty(e.getPlayer().getName()).size() - 1, cur = HPS.SpellManager.getCurrentSpellPosition(e.getPlayer()), neww;

                if (knows == -1 || cur == null) {
                    HPS.PM.tell(e.getPlayer(), HPS.Localisation.getTranslation("genKnowNoSpells"));
                    return;
                }

                if (e.getPlayer().isSneaking()) {
                    if (cur == 0)
                        neww = knows;
                    else
                        neww = cur - 1;
                } else {
                    if (cur == knows)
                        neww = 0;
                    else
                        neww = cur + 1;
                }

                try {
                    HPS.PM.newSpell(e.getPlayer(), HPS.SpellManager.setCurrentSpellPosition(e.getPlayer(), neww).getName());
                    if (HPS.getConfig().getBoolean("wand.lore.show-current-spell")) {
                    	 //TODO: Somehow get the Wand.Lore and update it cleanly instead of this
                        ItemStack wand = HPS.WandManager.getWandFromInventory(e.getPlayer().getInventory());
                        ItemMeta meta = wand.getItemMeta();
                        List<String> lore = meta.getLore();
                        for (String string : lore) {
                        	if (ChatColor.stripColor(string).contains("Current Spell: ")) {
                        		int index = lore.indexOf(string);
                        		Spell spell = HPS.SpellManager.getCurrentSpell(e.getPlayer());
                        		for (String line : HPS.getConfig().getStringList("wand.lore.format")) {
                        			if (ChatColor.stripColor(line).contains("%spell")) {
                        				line = ChatColor.translateAlternateColorCodes('&', line);
                        				line = line.replace("%spell", spell == null ? "None" : spell.getName());
                        				HPS.PM.debug(line);
                        				HPS.PM.debug("Current Spell position: " + neww);
                        				lore.set(index, line);
                        				break;
                        			}
                        		}
                        		break;
                        	}
                        }
                        meta.setLore(lore);
                        wand.setItemMeta(meta);
                    }
                } catch (IllegalArgumentException er) {
                    HPS.PM.tell(e.getPlayer(), HPS.Localisation.getTranslation("genKnowNoSpells"));
                } catch (NullPointerException er) {
                    PIE(e);
                }

                return;
            }

            //Cast spell
            if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                Spell currentSpell = HPS.SpellManager.getCurrentSpell(e.getPlayer());
                if (currentSpell != null)
                    HPS.SpellManager.cleverCast(e.getPlayer(), currentSpell);
            }

        }
    }

    @EventHandler
    public void PIEE(PlayerInteractEntityEvent e) {
    	for(String world : HPS.getConfig().getStringList("disabledWorlds")) if(e.getPlayer().getWorld().getName().toLowerCase().equals(world.toLowerCase())) return;
        HPS.PM.debug("Fired PlayerInteractEntityEvent");
        if (e.getPlayer().hasPermission(CAST_SPELLS) && HPS.WandManager.isWand(e.getPlayer().getInventory().getItemInMainHand())) {
            PlayerSpellConfig psc = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);
            Integer knows = psc.getStringListOrEmpty(e.getPlayer().getName()).size() - 1, cur = HPS.SpellManager.getCurrentSpellPosition(e.getPlayer()), neww;

            if (cur == null || knows == -1) {
                HPS.PM.tell(e.getPlayer(), HPS.Localisation.getTranslation("genKnowNoSpells"));
                return;
            }

            if (e.getPlayer().isSneaking()) {
                if (cur == 0)
                    neww = knows;
                else
                    neww = cur - 1;
            } else {
                if (cur == knows)
                    neww = 0;
                else
                    neww = cur + 1;
            }

            try {
                HPS.PM.newSpell(e.getPlayer(), HPS.SpellManager.setCurrentSpellPosition(e.getPlayer(), neww).getName());
            } catch (IllegalArgumentException er) {
                HPS.PM.tell(e.getPlayer(), HPS.Localisation.getTranslation("genKnowNoSpells"));
            } catch (NullPointerException er) {
                PIEE(e);
            }

            return;
        }
    }

    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent e) {
    	for(String world : HPS.getConfig().getStringList("disabledWorlds")) if(e.getPlayer().getWorld().getName().toLowerCase().equals(world.toLowerCase())) return;
        if (HPS.getConfig().getBoolean("spell-castable-with-chat")) {
            if (HPS.SpellManager.isSpell(e.getMessage().substring(0, e.getMessage().length() - 1))) {
                Bukkit.getScheduler().runTask(HPS, new Runnable() {

                    @Override
                    public void run() {
                        HPS.SpellManager.cleverCast(e.getPlayer(), HPS.SpellManager.getSpell(e.getMessage().substring(0, e.getMessage().length() - 1)));
                    }

                });
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent e) {
        if (HPS.WandManager.isWand(e.getRecipe().getResult())) {
        	e.setCurrentItem(HPS.WandManager.getWand((Player) e.getWhoClicked()));
            final Player p = (Player) e.getWhoClicked();
            Bukkit.getScheduler().runTask(HPS, new Runnable() {

                @Override
                public void run() {
                    p.updateInventory();
                }

            });
        }
    }
    
    @EventHandler(priority=EventPriority.LOWEST)
    public void onSpellCast(SpellPreCastEvent e) {
    	for(String world : HPS.getConfig().getStringList("disabledWorlds")) if(e.getCaster().getWorld().getName().toLowerCase().equals(world.toLowerCase())) return;
    	if (!e.getCaster().hasPermission(e.getSpell().getPermission())) {
    		e.setCancelled(true);
    		HPS.PM.warn(e.getCaster(), HPS.Localisation.getTranslation("spellUnauthorized"));
    	}
    }
    
}
