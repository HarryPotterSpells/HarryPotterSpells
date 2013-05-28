package com.lavacraftserver.HarryPotterSpells.API;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class SpellCastEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	private Spell spell;
	private Player caster;
	private boolean cancelled = false;
	
	public SpellCastEvent(Spell spell, Player caster) {
		this.spell = spell;
		this.caster = caster;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public Spell getSpell() {
		return spell;
	}
	
	public Player getCaster() {
		return caster;
	}
	
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
	
	/*
	 * START STATIC METHODS
	 */
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
