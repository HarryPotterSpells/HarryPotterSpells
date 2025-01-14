package com.hpspells.core.api;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.spell.SpellManager;

public class APIHandler {
	
	public static APIHandler instance;
	
	private HarryPotterSpells HPS;
	private SpellManager spellManager;
	
	public APIHandler(HarryPotterSpells HPS) {
		instance = this;
		this.HPS = HPS;
		spellManager = HPS.SpellManager;
	}
	
	HarryPotterSpells getHPS() {
		return HPS;
	}
	
	public static APIHandler getInstance() {
		return instance;
	}
	
	public static SpellManager getSpellManager() {
		return APIHandler.getInstance().spellManager;
	}
}
