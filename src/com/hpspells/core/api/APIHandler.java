package com.hpspells.core.api;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.SpellManager;

public class APIHandler {
	
	public static APIHandler instance;
	
	private HPS HPS;
	private SpellManager spellManager;
	
	public APIHandler(HPS HPS) {
		instance = this;
		this.HPS = HPS;
		spellManager = HPS.SpellManager;
	}
	
	HPS getHPS() {
		return HPS;
	}
	
	public static APIHandler getInstance() {
		return instance;
	}
	
	public static SpellManager getSpellManager() {
		return APIHandler.getInstance().spellManager;
	}
}
