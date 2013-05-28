package com.lavacraftserver.HarryPotterSpells;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lavacraftserver.HarryPotterSpells.API.SpellCastEvent;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class Listeners implements Listener {
	public HashMap<String, Integer> currentSpell = new HashMap<String, Integer>();
	
	@EventHandler
	public void PIE(PlayerInteractEvent e) {
		if(HPS.PM.hasPermission("HarryPotterSpells.use", e.getPlayer()) && HPS.Wand.isWand(e.getPlayer().getItemInHand())) {
			//Change spell
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Player p = e.getPlayer();
				List<String> spellList = HPS.PlayerSpellConfig.getPSC().getStringList(p.getName());
				if(spellList == null || spellList.isEmpty()) {
					HPS.PM.tell(p, "You don't know any spells.");
					return;
				}
				int spellNumber = 0, max = spellList.size() - 1;
				if(currentSpell.containsKey(p.getName())) {
					if(p.isSneaking()) {
						if(currentSpell.get(p.getName()) == 0) {
							spellNumber = max;
						} else {
							spellNumber = currentSpell.get(p.getName()) - 1;
						}
					} else if(!(currentSpell.get(p.getName()) == max)) {
						spellNumber = currentSpell.get(p.getName()) + 1;
					}
				}
				HPS.PM.newSpell(p, spellList.get(spellNumber));
				currentSpell.put(p.getName(), spellNumber);
				return;
			}
			
			//Cast spell
			if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
				Player p = e.getPlayer();
				List<String> spellList = HPS.PlayerSpellConfig.getPSC().getStringList(p.getName());
				if(spellList == null || spellList.isEmpty()) {
					HPS.PM.tell(p, "You don't know any spells.");
					return;
				}
				
				int spellNumber = 0;
				if(currentSpell.containsKey(p.getName()))
					spellNumber = currentSpell.get(p.getName());
	
				if(HPS.Plugin.getConfig().getBoolean("spell-particle-toggle")) {
					Location l = p.getLocation();
					l.setY(l.getY() + 1);
					p.getWorld().playEffect(l, Effect.ENDER_SIGNAL, 0);
				}
				Spell spell = HPS.SpellManager.getSpell(spellList.get(spellNumber));
				
				SpellCastEvent sce = new SpellCastEvent(spell, p);
				Bukkit.getServer().getPluginManager().callEvent(sce);
				if(!sce.isCancelled())
					spell.cast(p);

				//Cancel event if player is in creative to prevent block damage.
				if (p.getGameMode().equals(GameMode.CREATIVE))
					e.setCancelled(true);
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
		if(HPS.PM.hasPermission("HarryPotterSpells.use", e.getPlayer()) && HPS.Wand.isWand(e.getPlayer().getItemInHand())) {
			Player p = e.getPlayer();
			List<String> spellList = HPS.PlayerSpellConfig.getPSC().getStringList(p.getName());
			if(spellList == null || spellList.isEmpty()) {
				HPS.PM.tell(p, "You don't know any spells.");
				return;
			}
			int spellNumber = 0, max = spellList.size() - 1;
			if(currentSpell.containsKey(p.getName())) {
				if(p.isSneaking()) {
					if(currentSpell.get(p.getName()) == 0) {
						spellNumber = max;
					} else {
						spellNumber = currentSpell.get(p.getName()) - 1;
					}
				} else if(!(currentSpell.get(p.getName()) == max)) {
					spellNumber = currentSpell.get(p.getName()) + 1;
				}
			}
			HPS.PM.newSpell(p, spellList.get(spellNumber));
			currentSpell.put(p.getName(), spellNumber);
			return;
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
