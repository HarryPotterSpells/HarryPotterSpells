package com.hpspells.core.api.event;

import com.hpspells.core.spell.Spell;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Event called before a spell is cast
 */
public class SpellPreCastEvent extends SpellEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    public SpellPreCastEvent(Spell spell, Player caster) {
        super(spell, caster);
    }

    /**
     * Gets the HandlerList for this event
     *
     * @return the handler list
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets the cancelation status of this spell
     *
     * @return {@code true} if the spell has been cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancelation status of this spell
     *
     * @param cancel
     */
    @Override
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
