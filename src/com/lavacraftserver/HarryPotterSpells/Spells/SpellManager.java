package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.ArrayList;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;

public class SpellManager {
	private ArrayList<Spell> spellList;
	HarryPotterSpells plugin;
	
	public SpellManager(HarryPotterSpells instance){
		plugin=instance;
		spellList=new ArrayList<Spell>();
		spellList.add(new Aguamenti(plugin));
		spellList.add(new AlarteAscendare(plugin));
		spellList.add(new Alohomora(plugin));
		spellList.add(new AvadaKedavra(plugin));
		spellList.add(new Confringo(plugin));
		spellList.add(new Confundo(plugin));
		spellList.add(new Deprimo(plugin));
		spellList.add(new Episkey(plugin));
		spellList.add(new Evanesco(plugin));
		spellList.add(new Expelliarmus(plugin));
		spellList.add(new Incendio(plugin));
		spellList.add(new Multicorfors(plugin));
		spellList.add(new Obscuro(plugin));
		spellList.add(new Orchideous(plugin));
		spellList.add(new PetrificusTotalus(plugin));
		spellList.add(new Reducto(plugin));
		spellList.add(new Sectumsempra(plugin));
		spellList.add(new Sonorus(plugin));
		spellList.add(new Spongify(plugin));
		spellList.add(new TimeSpell(plugin));
		spellList.add(new TreeSpell(plugin));
		spellList.add(new WingardiumLeviosa(plugin));
	}
	
	public Spell getSpell(String name) {
		for(Spell spell:spellList){
			if(spell.getName().equalsIgnoreCase(name)||spell.toString().equalsIgnoreCase(name)){
				return spell;
			}
		}
		return new InvalidSpell(plugin);
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

}
