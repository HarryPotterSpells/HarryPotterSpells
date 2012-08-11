package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.ArrayList;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Aguamenti;
import com.lavacraftserver.HarryPotterSpells.Spells.AlarteAscendare;
import com.lavacraftserver.HarryPotterSpells.Spells.AvadaKedavra;
import com.lavacraftserver.HarryPotterSpells.Spells.Confringo;
import com.lavacraftserver.HarryPotterSpells.Spells.Confundo;
import com.lavacraftserver.HarryPotterSpells.Spells.Deprimo;
import com.lavacraftserver.HarryPotterSpells.Spells.Episkey;
import com.lavacraftserver.HarryPotterSpells.Spells.Evanesco;
import com.lavacraftserver.HarryPotterSpells.Spells.Expelliarmus;
import com.lavacraftserver.HarryPotterSpells.Spells.Incendio;
import com.lavacraftserver.HarryPotterSpells.Spells.InvalidSpell;
import com.lavacraftserver.HarryPotterSpells.Spells.Multicorfors;
import com.lavacraftserver.HarryPotterSpells.Spells.Reducto;
import com.lavacraftserver.HarryPotterSpells.Spells.Sonorus;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;
import com.lavacraftserver.HarryPotterSpells.Spells.Spongify;
import com.lavacraftserver.HarryPotterSpells.Spells.TimeSpell;
import com.lavacraftserver.HarryPotterSpells.Spells.TreeSpell;
import com.lavacraftserver.HarryPotterSpells.Spells.WingardiumLeviosa;

public class SpellManager {

private ArrayList<Spell> spellList;
HarryPotterSpells plugin;
	public SpellManager(HarryPotterSpells instance){
		plugin=instance;
		spellList=new ArrayList<Spell>();
		spellList.add(new Aguamenti(plugin));
		spellList.add(new AlarteAscendare(plugin));
		spellList.add(new AvadaKedavra(plugin));
		spellList.add(new Confringo(plugin));
		spellList.add(new Confundo(plugin));
		spellList.add(new Deprimo(plugin));
		spellList.add(new Episkey(plugin));
		spellList.add(new Evanesco(plugin));
		spellList.add(new Expelliarmus(plugin));
		spellList.add(new Incendio(plugin));
		spellList.add(new Multicorfors(plugin));
		spellList.add(new Reducto(plugin));
		spellList.add(new Sonorus(plugin));
		spellList.add(new Spongify(plugin));
		spellList.add(new TimeSpell(plugin));
		spellList.add(new TreeSpell(plugin));
		spellList.add(new WingardiumLeviosa(plugin));
}
	public Spell getSpell(String name){
		for(Spell spell:spellList){
			if(spell.getName().equalsIgnoreCase(name)||spell.toString().equalsIgnoreCase(name)){
				return spell;
			}
		}
		return new InvalidSpell(plugin);
	}
	
	public void addSpell(Spell spell){
		spellList.add(spell);
	}
	
	public ArrayList<Spell> getSpells(){
		return spellList;
	}
	

}
