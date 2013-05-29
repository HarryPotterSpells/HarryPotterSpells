package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.reflections.Reflections;

import com.lavacraftserver.HarryPotterSpells.HPS;

public class SpellManager {
	private ArrayList<Spell> spellList = new ArrayList<Spell>();
	
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
	
	public Spell getSpell(String name) {
		for(Spell spell:spellList){
			if(spell.getName().equalsIgnoreCase(name)||spell.toString().equalsIgnoreCase(name)){
				return spell;
			}
		}
		return new InvalidSpell();
	}
	
	public void addSpell(Spell spell) {
		spellList.add(spell);
	}
	
	public ArrayList<Spell> getSpells() {
		return spellList;
	}
	
	public boolean isSpell(String name) {
		for(Spell spell:spellList){
			if(spell.getName().equalsIgnoreCase(name)||spell.toString().equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
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
