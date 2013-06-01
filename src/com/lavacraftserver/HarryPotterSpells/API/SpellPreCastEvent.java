package com.lavacraftserver.HarryPotterSpells.API;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

/**
 * Event called before a spell is cast
 */
public class SpellPreCastEvent extends SpellEvent {
	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled = false;
	
	public SpellPreCastEvent(Spell spell, Player caster) {
		super(spell, caster);
	}

	/**
	 * Gets the HandlerList for this event
	 * @return the handler list
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	/**
	 * Gets the cancelation status of this spell
	 * @return {@code true} if the spell has been cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * Sets the cancelation status of this spell
	 * @param cancel
	 */
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
