package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;
import org.reflections.Reflections;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.API.SpellCastEvent;

/**
 * A class that manages spells and holds lots of spell related utilities
 */
public class SpellManager {
	private ArrayList<Spell> spellList = new ArrayList<Spell>();
	public HashMap<String, Integer> currentSpell = new HashMap<String, Integer>();
	
	public SpellManager(){
		Reflections ref = new Reflections("com.lavacraftserver.HarryPotterSpells.Spells");
		for(Class<?> clazz : ref.getTypesAnnotatedWith(Spell.spell.class)) {
			Spell spell;
			if(clazz == Spell.class || clazz == InvalidSpell.class || !Spell.class.isAssignableFrom(clazz))
				continue;
			try {
				//spell = (Spell) clazz.getConstructor(HPS.class).newInstance();
				spell = (Spell) clazz.newInstance();
			} catch (Exception e) {
				HPS.PM.log("An error occurred whilst adding the " + clazz.getName() + " spell to the spell list. That spell will not be available." , Level.WARNING);
				e.printStackTrace();
				continue;
			}
			spellList.add(spell);
		}
		
		load();
	}
	
	/**
	 * Gets a spell by name
	 * @param name the spell to get
	 * @return the spell or an {@link InvalidSpell} if not found
	 */
	public Spell getSpell(String name) {
		for(Spell spell:spellList){
			if(spell.getName().equalsIgnoreCase(name)||spell.toString().equalsIgnoreCase(name)){
				return spell;
			}
		}
		return new InvalidSpell();
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
		for(Spell spell:spellList){
			if(spell.getName().equalsIgnoreCase(name)||spell.toString().equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Casts a spell cleverly; checking permissions, sending effects ect
	 * @param player the player who is casting
	 * @param spell the spell that they are casting
	 */
	public void cleverCast(Player player, Spell spell) {
	    if(!player.hasPermission("HarryPotterSpells.use") || !spell.playerKnows(player))
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
        
        SpellCastEvent sce = new SpellCastEvent(spell, player);
        Bukkit.getServer().getPluginManager().callEvent(sce);
        if(!sce.isCancelled())
            spell.cast(player);
	}
	
	/**
	 * Gets the spell a player has currently selected
	 * @param player the player
	 * @return the spell they are on
	 */
	public Spell getCurrentSpell(Player player) {
	    return getSpell(HPS.PlayerSpellConfig.getPSC().getStringList(player.getName()).get(currentSpell.get(player.getName())));
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
			configSpells.createSection(s.getInternalName());
			configSpells.set(s.getInternalName(), s.save(configSpells.getConfigurationSection(s.getInternalName())));
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
