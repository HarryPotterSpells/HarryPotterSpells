package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.reflections.Reflections;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.API.SpellPostCastEvent;
import com.lavacraftserver.HarryPotterSpells.API.SpellPreCastEvent;
import com.lavacraftserver.HarryPotterSpells.Utils.CoolDown;

/**
 * A class that manages spells and holds lots of spell related utilities
 */
public class SpellManager {
	private ArrayList<Spell> spellList = new ArrayList<Spell>();
	public HashMap<String, Integer> currentSpell = new HashMap<String, Integer>();
	private HashMap<String, HashMap<Spell, Integer>> cooldowns = new HashMap<String, HashMap<Spell, Integer>>();
	
	/**
	 * Constructs the {@link SpellManager}, adding all core spells to the Spell List
	 */
	public SpellManager() {
		Reflections ref = new Reflections("com.lavacraftserver.HarryPotterSpells.Spells");
		for(Class<?> clazz : ref.getTypesAnnotatedWith(Spell.spell.class)) {
			Spell spell;
			if(clazz == Spell.class || !Spell.class.isAssignableFrom(clazz))
				continue;
			try {
				spell = (Spell) clazz.newInstance();
			} catch (Exception e) {
				HPS.PM.log(Level.WARNING, "An error occurred whilst adding the " + clazz.getName() + " spell to the spell list. That spell will not be available." );
				HPS.PM.debug(e);
				continue;
			}
			spellList.add(spell);
		}
		
		load();
	}
	
	/**
	 * Gets a spell by name
	 * @param name the spell to get
	 * @return the spell or {@code null} if not found
	 */
	public Spell getSpell(String name) {
		for(Spell spell:spellList)
			if(spell.getName().equalsIgnoreCase(name))
				return spell;
		return null;
	}
	
	/**
	 * Adds a spell to the spell list
	 * @param spell the spell
	 */
	public void addSpell(Spell spell) {
		spellList.add(spell);
	}
	
	/**
	 * Gets a list of all spells
	 * @return the list
	 */
	public ArrayList<Spell> getSpells() {
		return spellList;
	}
	
	/**
	 * Checks if a string corrosponds to a spell
	 * @param name the name to test
	 * @return {@code true} if the spell exists
	 */
	public boolean isSpell(String name) {
		return getSpell(name) != null;
	}
	
	/**
	 * Casts a spell cleverly; checking permissions, sending effects ect
	 * @param player the player who is casting
	 * @param spell the spell that they are casting
	 */
	public void cleverCast(Player player, Spell spell) {
	    if(!player.hasPermission("HarryPotterSpells.use") || !spell.playerKnows(player) || !player.getInventory().contains(Material.STICK))
	        return;
	    
        List<String> spellList = HPS.PlayerSpellConfig.getPSC().getStringList(player.getName());
        if(spellList == null || spellList.isEmpty()) {
            HPS.PM.tell(player, "You don't know any spells.");
            return;
        }
        
        if(HPS.Plugin.getConfig().getBoolean("spell-particle-toggle")) {
            Location l = player.getLocation();
            l.setY(l.getY() + 1);
            player.getWorld().playEffect(l, Effect.ENDER_SIGNAL, 0);
        }
        
        SpellPreCastEvent sce = new SpellPreCastEvent(spell, player);
        Bukkit.getServer().getPluginManager().callEvent(sce);
        if(!sce.isCancelled()){
        	Boolean cast = true;
            String playerName = player.getName();
            if(cooldowns.containsKey(playerName) && cooldowns.get(playerName).containsKey(spell)) { // TODO move to another class
            	HPS.PM.dependantMessagingTell((CommandSender) player, "You must wait " + cooldowns.get(playerName).get(spell).toString() + " seconds before performing this spell again.");
            	cast = false;
            }
            
            boolean successful = spell.cast(player);
            Bukkit.getServer().getPluginManager().callEvent(new SpellPostCastEvent(spell, player, successful));
            
            if(cast && successful && spell.getCoolDown() > 0 && !player.hasPermission("HarryPotterSpells.nocooldown")){
            	setCoolDown(playerName, spell, spell.getCoolDown());
            	Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS.Plugin, new CoolDown(playerName, spell), 20L);
            }
        }
	}
	
	/**
	 * Gets the spell a player has currently selected
	 * @param player the player
	 * @return the spell they are on
	 */
	public Spell getCurrentSpell(Player player) {
	    return currentSpell.containsKey(player.getName()) ? getSpell(HPS.PlayerSpellConfig.getPSC().getStringList(player.getName()).get(currentSpell.get(player.getName()))) : null;
	}
	
	/**
	 * Gets the cooldown of a certain spell for a certain player
	 * @param playerName
	 * @param spell
	 * @return
	 */
	public Integer getCoolDown(String playerName, Spell spell){
		return cooldowns.get(playerName).get(spell);
	}
	
	/**
	 * Sets the cooldown of a certain spell for a certain player
	 * @param playerName
	 * @param spell
	 * @param cooldown
	 */
	public void setCoolDown(String playerName, Spell spell, Integer cooldown) {
	    if(cooldown == null)
	        return;
	    
		if (cooldowns.containsKey(playerName) && cooldowns.get(playerName).containsKey(spell))
			cooldowns.get(playerName).put(spell, cooldown);
		else if (cooldowns.containsKey(playerName) && !cooldowns.get(playerName).containsKey(spell))
			cooldowns.get(playerName).put(spell, cooldown);
		else {
           	cooldowns.put(playerName, new HashMap<Spell, Integer>());
        	cooldowns.get(playerName).put(spell, cooldown);
		}
	}
	
	/**
	 * Changes the current spell of a player
	 * @param player the player
	 * @param forward {@code true} if they are moving forward. {@code false} if backwards.
	 */
	public void changeCurrentSpell(Player player, boolean forward) {
        List<String> spellList = HPS.PlayerSpellConfig.getPSC().getStringList(player.getName());
        if(spellList == null || spellList.isEmpty()) {
            HPS.PM.tell(player, "You don't know any spells.");
            return;
        }
        int spellNumber = 0, max = spellList.size() - 1;
        if(currentSpell.containsKey(player.getName())) {
            if(!forward) {
                if(currentSpell.get(player.getName()) == 0)
                    spellNumber = max;
                else
                    spellNumber = currentSpell.get(player.getName()) - 1;
            } else if(!(currentSpell.get(player.getName()) == max))
                spellNumber = currentSpell.get(player.getName()) + 1;
        }
        HPS.PM.newSpell(player, spellList.get(spellNumber));
        currentSpell.put(player.getName(), spellNumber);
        return;
	}
	
	public void save() {
		HPS.Plugin.getConfig().createSection("spells");
		ConfigurationSection configSpells = HPS.Plugin.getConfig().getConfigurationSection("spells");
		for(Spell s : spellList) {
			configSpells.createSection(s.getName());
			configSpells.set(s.getName(), s.save(configSpells.getConfigurationSection(s.getName())));
		}
		HPS.Plugin.getConfig().set("arenas", configSpells);
	}

	public void load() {
		try{
			if(!HPS.Plugin.getConfig().isSet("spells"))
				HPS.Plugin.getConfig().getConfigurationSection("spells");
			ConfigurationSection configSpells = HPS.Plugin.getConfig().getConfigurationSection("spells");
			for(String k : configSpells.getKeys(false))
				getSpell(k).load(configSpells.getConfigurationSection(k));
		} catch(Exception e){
			//TODO catch this exception
		}
	}
	
}
