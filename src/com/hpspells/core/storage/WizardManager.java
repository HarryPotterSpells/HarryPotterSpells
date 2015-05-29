package com.hpspells.core.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.hpspells.core.HPS;
import com.hpspells.core.configuration.ConfigurationManager.ConfigurationType;
import com.hpspells.core.configuration.PlayerSpellConfig;

public class WizardManager {
	
	private static HPS HPS;
	private static List<Wizard> wizardList = new ArrayList<Wizard>();
	
	public WizardManager(HPS HPS) {
		WizardManager.HPS = HPS;
	}
	
	public static void saveWizard(Player player) {
		final Wizard wizard = getWizard(player.getUniqueId());
		final PreparedStatement updateStatement;
		BukkitRunnable saveRunnable;
		switch(HPS.dbManager.getStorageFormat()) {
		case MYSQL:
		case SQLITE:
			try {
				updateStatement = HPS.dbManager.getDatabase().prepareStatement(
						"INSERT INTO hps_users (uuid, playername, spells, currentpos)"
						+ "VALUES (?,?,?,?)"
						+ "ON DUPLICATE KEY UPDATE playername=VALUES(playername),spells=VALUES(spells),currentpos=VALUES(currentpos)"
				);
				updateStatement.setString(1, wizard.getUUID().toString());
				updateStatement.setString(2, wizard.getName());
				String spellsList = "";
				for (String string : wizard.getKnownSpellsList()) {
					spellsList += (spellsList != "" ? "," : "") + string;
				}
				updateStatement.setString(3, spellsList);
				updateStatement.setInt(4, wizard.getCurrentSpellPosition());
			} catch (SQLException e) {
				HPS.dbManager.getDatabase().printErr("Unable to save player: " + player.getName(), true);
				HPS.PM.debug(e);
				return;
			}
			saveRunnable = new BukkitRunnable() {
				public void run() {
					try {
						updateStatement.executeQuery();
					} catch (SQLException e) {
						HPS.dbManager.getDatabase().printErr("Unable to save player: " + wizard.getName(), true);
						HPS.PM.debug(e);
					}
				}
			};
			saveRunnable.runTaskAsynchronously(HPS);
			break;
		default:
			PlayerSpellConfig psc = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);
			psc.get().set(wizard.getUUID().toString() + ".name", wizard.getName());
			psc.get().set(wizard.getUUID().toString() + ".spells", wizard.getKnownSpellsList());
	        psc.get().set(wizard.getUUID().toString() + ".currentpos", wizard.getCurrentSpellPosition());
	        psc.save();
			break;
		}
	}
	
	public static Wizard loadWizard(final UUID uuid, final String name) {
		if (getWizard(uuid) != null) {
			return getWizard(uuid);
		}
		final Wizard wizard = new Wizard(uuid, name);
		final PreparedStatement loadStatement;
		BukkitRunnable loadRunnable;
		switch(HPS.dbManager.getStorageFormat()) {
		case MYSQL:
		case SQLITE:
			try {
				loadStatement = HPS.dbManager.getDatabase().prepareStatement(
						"SELECT * FROM hps_users WHERE uuid = '?'"
				);
				loadStatement.setString(1, uuid.toString());
			} catch (SQLException e) {
				HPS.dbManager.getDatabase().printErr("Unable to load player: " + name, true);
				HPS.PM.debug(e);
				return null;
			}
			loadRunnable = new BukkitRunnable() {
				public void run() {
					try {
						ResultSet rs = loadStatement.executeQuery();
						if (rs.next()) {
							wizard.setKnownSpellsList(rs.getString("spells") != null ? Arrays.asList(rs.getString("spells")) : new ArrayList<String>());
//							if (rs.getString("spells") != null || rs.getString("spells") != "") {
//								List<String> spellsList = new ArrayList<String>(Arrays.asList(rs.getString("spells").split(",")));
//							}
							wizard.setCurrentSpellPosition((Integer)rs.getInt("currentpos") != null ? rs.getInt("currentpos") : -1);
						}
					} catch (SQLException e) {
						HPS.dbManager.getDatabase().printErr("Unable to save player: " + name, true);
						HPS.PM.debug(e);
					}
				}
			};
			loadRunnable.runTaskAsynchronously(HPS);
			break;
		default:
			PlayerSpellConfig psc = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);
	        if (psc.get().getConfigurationSection(uuid.toString()) != null) {
	        	HPS.PM.debug("Loading player: " + uuid);
	        	HPS.PM.debug("CurrentSpell pos: " + psc.get().getInt(uuid.toString() + ".currentpos"));
	        	wizard.setKnownSpellsList(psc.getStringListOrEmpty(uuid.toString() + ".spells"));
		        wizard.setCurrentSpellPosition(psc.get().getInt(uuid.toString() + ".currentpos"));
	        } else {
	        	HPS.PM.debug("ConfigSection not found for: " + uuid);
	        	psc.get().set(uuid.toString() + ".name", name);
	        	wizard.setKnownSpellsList(new ArrayList<String>());
	        	psc.save();
	        }
			break;
		}
		wizardList.add(wizard);
		wizard.setCurrentSpell(HPS.SpellManager.getCurrentSpell(uuid));
		updateWizard(wizard);
		return wizard;
	}
	
    public static Wizard getWizard(UUID uuid) {
    	for (Wizard wizard : wizardList) {
    		if (wizard.getUUID() == uuid) {
    			return wizard;
    		}
    	}
    	return null;
    }
    
    public static void updateWizard(Wizard wizard) {
    	if (getWizard(wizard.getUUID()) != null) {
    		wizardList.remove(getWizard(wizard.getUUID()));
    		wizardList.add(wizard);
    	} else {
    		wizardList.add(wizard);
    	}
    }
	
	public List<Wizard> getWizardList() {
		return wizardList;
	}
}
