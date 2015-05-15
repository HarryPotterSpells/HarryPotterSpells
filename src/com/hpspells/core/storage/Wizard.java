package com.hpspells.core.storage;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.hpspells.core.spell.Spell;

/**
 * Represents a player that can casts spells.
 * <p>
 * Contains spell related information about the player.
 * </p>
 */
public class Wizard {
	
	private UUID UUID;
	private String name;
	
	private List<String> knownSpellsList;
	private Spell currentSpell;
	private int currentSpellPosition;
	
	public Wizard(UUID uuid) {
		this(uuid, Bukkit.getPlayer(uuid).getName());
	}
	
	public Wizard(UUID uuid, String name) {
		this.UUID = uuid;
		this.name = name;
	}
	
	public void setCurrentSpell(Spell spell) {
		this.currentSpell = spell;
	}

	public void setCurrentSpellPosition(int position) {
		this.currentSpellPosition = position;
	}
	
    /**
     * Checks if this {@link Wizard} knows a certain spell.
     * 
     * @param spell The spell to check
     * @return true if spell is known
     */
	public boolean knowsSpell(Spell spell) {
		if (knownSpellsList.contains(spell)) {
			return true;
		}
		return false;
	}
	
	public UUID getUUID() {
		return UUID;
	}

	public String getName() {
		return name;
	}

	public List<String> getKnownSpellsList() {
		return knownSpellsList;
	}
	
	public void setKnownSpellsList(List<String> knownSpellList) {
		this.knownSpellsList = knownSpellList;
	}
	
	/**
     * Gets the current spell of this {@link Wizard}.
     * 
     * @return the Spell object
     */
	public Spell getCurrentSpell() {
		return currentSpell;
	}

	/**
     * Gets the current spell position of this {@link Wizard}.
     *
     * @return the current spell position they are on
     * <br>
     * OR
     * <br>
     * -1 if they don't know any spells
     * <br>
     * 0 if they don't have a current spell 
     */
	public int getCurrentSpellPosition() {
		if (knownSpellsList.isEmpty()) {
			return -1;
		}
		if (getCurrentSpell() == null) {
			return 0;
		}
		return currentSpellPosition;
	}

}
