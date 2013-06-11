package com.lavacraftserver.HarryPotterSpells;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class Listeners implements Listener {
    private HPS HPS;
    
    public Listeners(HPS plugin) {
        HPS = plugin;
    }
    	
	@EventHandler
	public void PIE(PlayerInteractEvent e) {
		if(e.getPlayer().hasPermission("HarryPotterSpells.use") && HPS.Wand.isWand(e.getPlayer().getItemInHand())) {
		    
            if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
                e.setCancelled(true);
		    
			//Change spell
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			    Integer knows = HPS.PlayerSpellConfig.getStringListOrEmpty(e.getPlayer().getName()).size() - 1, cur = HPS.SpellManager.getCurrentSpellPosition(e.getPlayer()), neww;
			    			    
			    if(HPS.PlayerSpellConfig.getStringListOrEmpty(e.getPlayer().getName()).isEmpty() || cur == null) {
			        HPS.PM.tell(e.getPlayer(), HPS.Localisation.getTranslation("genKnowNoSpells"));
			        return;
			    }
			    
			    if(e.getPlayer().isSneaking()) {
			        if(cur == 0)
			            neww = knows;
			        else
			            neww = cur - 1;
			    } else {
			        if(cur == knows)
			            neww = 0;
			        else
			            neww = cur + 1;
			    }
			    
			    try {
			        HPS.PM.newSpell(e.getPlayer(), HPS.SpellManager.setCurrentSpell(e.getPlayer(), neww).getName());
			    } catch(IllegalArgumentException er) {
                    HPS.PM.tell(e.getPlayer(), HPS.Localisation.getTranslation("genKnowNoSpells"));
                    return;
			    }
			    
			    return;
			}
			
			//Cast spell
			if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
				Spell currentSpell = HPS.SpellManager.getCurrentSpell(e.getPlayer());
				if(currentSpell != null)
					HPS.SpellManager.cleverCast(e.getPlayer(), currentSpell);
			}
			
		}
		
		//TODO MOVE TO VAULT ADDON
		//Spell sign
		/*
		if (e.getClickedBlock() != null && plugin.PM.hasPermission("HarryPotterSpells.buyfromsign", e.getPlayer()) && e.getClickedBlock().getType() == Material.SIGN) {
			String[] signText = ((Sign)e.getClickedBlock()).getLines();
			String identifier = "[" + ChatColor.GREEN + plugin.getConfig().getString("SpellSigns.textForLine1") + ChatColor.RESET + "]";
			if(signText[0].equals(identifier)) {
				double amount = (double)Integer.parseInt(signText[2]);
				Spell spell = plugin.SpellManager.getSpell(signText[1]);
				EconomyResponse r = plugin.Vault.econ.depositPlayer(e.getPlayer().getName(), amount);
				if(spell.playerKnows(e.getPlayer())) {
					plugin.PM.warn(e.getPlayer(), "You already know that spell.");
				} else {
					if(r.transactionSuccess()) {
						plugin.PM.tell(e.getPlayer(), plugin.Vault.econ.format(amount) + " has been deducted from your account.");
						spell.teach(e.getPlayer());
					} else {
						plugin.PM.warn(e.getPlayer(), "An error occured during the transation: " + r.errorMessage);
					}
				}
			}
		}
		*/
	}
	
	@EventHandler
	public void PIEE(PlayerInteractEntityEvent e) {
		if(e.getPlayer().hasPermission("HarryPotterSpells.use") && HPS.Wand.isWand(e.getPlayer().getItemInHand())) {
		    Integer knows = HPS.PlayerSpellConfig.getStringListOrEmpty(e.getPlayer().getName()).size() - 1, cur = HPS.SpellManager.getCurrentSpellPosition(e.getPlayer()), neww;
            
            if(cur == null) {
                HPS.PM.tell(e.getPlayer(), HPS.Localisation.getTranslation("genKnowNoSpells"));
                return;
            }
            
            if(e.getPlayer().isSneaking()) {
                if(cur == 0)
                    neww = knows;
                else
                    neww = cur - 1;
            } else {
                if(cur == knows)
                    neww = 0;
                else
                    neww = cur + 1;
            }
            
            try {
                HPS.PM.newSpell(e.getPlayer(), HPS.SpellManager.setCurrentSpell(e.getPlayer(), neww).getName());
            } catch(IllegalArgumentException er) {
                HPS.PM.tell(e.getPlayer(), HPS.Localisation.getTranslation("genKnowNoSpells"));
                return;
            }
            
            return;
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if(HPS.getConfig().getBoolean("spell-castable-with-chat")) {
			if(HPS.SpellManager.isSpell(e.getMessage().substring(0, e.getMessage().length() - 1))) {
				HPS.SpellManager.cleverCast(e.getPlayer(), HPS.SpellManager.getSpell(e.getMessage().substring(0, e.getMessage().length() - 1)));
				return;
			}
		}
	}
	
	/*
	@EventHandler
	public void onPlayerBlockPlace(BlockPlaceEvent e) {
		if(plugin.PM.hasPermission("HarryPotterSpells.sign.create", e.getPlayer()) && e.getBlockPlaced().getType() == Material.SIGN) {
			Sign sign = (Sign)e.getBlockPlaced();
			String identifier = "[" + plugin.getConfig().getString("SpellSigns.textForLine1") + "]";
			String[] signText = sign.getLines();
			boolean error = false;
			if(signText[0].equals(identifier)) {
				if(!plugin.SpellManager.isSpell(signText[1])) {
					sign.setLine(1, "[" + ChatColor.RED + plugin.getConfig().getString("SpellSigns.textForLine1") + ChatColor.RESET + "]");
					plugin.PM.warn(e.getPlayer(), "That spell was not recognised.");
					error = true;
				} else {
					error = false;
				}
				if(!Double.isNaN((double)Integer.parseInt(signText[2]))) {
					sign.setLine(1, "[" + ChatColor.RED + plugin.getConfig().getString("SpellSigns.textForLine1") + ChatColor.RESET + "]");
					plugin.PM.warn(e.getPlayer(), "The price is not a number.");
					error = true;
				} else {
					error = false;
				}
				if(!error) {
					sign.setLine(1, "[" + ChatColor.GREEN + plugin.getConfig().getString("SpellSigns.textForLine1") + ChatColor.RESET + "]");
					plugin.PM.tell(e.getPlayer(), "SpellSign created!");
				}
				sign.update();
			}
		}
	}
	*/ //TODO move to addon

}
