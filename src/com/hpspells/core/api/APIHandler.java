package com.hpspells.core.api;

import java.util.List;

import org.bukkit.entity.Player;

import com.hpspells.core.HPS;
import com.hpspells.core.configuration.ConfigurationManager.ConfigurationType;
import com.hpspells.core.configuration.PlayerSpellConfig;
import com.hpspells.core.spell.SpellManager;

public class APIHandler {
	
	private static APIHandler instance;
	
	private HPS HPS;
	private SpellManager spellManager;
	private PlayerSpellConfig PSC;
	
	public APIHandler(HPS HPS) {
		instance = this;
		this.HPS = HPS;
		this.spellManager = HPS.SpellManager;
		this.PSC = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);
	}
	
	HPS getHPS() {
		return HPS;
	}
	
	public static APIHandler getInstance() {
		return instance;
	}
	
	public SpellManager getSpellManager() {
		return spellManager;
	}
	
	public PlayerSpellConfig getPlayerSpellConfig() {
		return PSC;
	}
	
	public List<String> getKnownSpells(Player player) {
		return PSC.getStringListOrEmpty(player.getName());
	}
	
}
