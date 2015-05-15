package com.hpspells.core.storage;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class WizardManager {
	
	private List<Wizard> wizardList = new ArrayList<Wizard>();
	
	public WizardManager() {}
	
    public Wizard getWizard(Player player) {
    	for (Wizard wizard : wizardList) {
    		if (Bukkit.getPlayer(wizard.getUUID()) == player) {
    			return wizard;
    		}
    	}
    	return null;
    }
	
	public List<Wizard> getWizardList() {
		return wizardList;
	}
}
