package com.hpspells.core.api.event;

import com.hpspells.core.spell.Spell;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class SpellPostCastEvent extends SpellEvent {
    private static final HandlerList handlers = new HandlerList();

    private final boolean successful;

    public SpellPostCastEvent(Spell spell, Player caster, boolean successful) {
        super(spell, caster);
        this.successful = successful;
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
     * Gets whether the spell was successfully cast or not
     *
     * @return {@code true} if the spell was successfully cast
     */
    public boolean wasSuccessful() {
        return successful;
    }

    /*
     * START STATIC METHODS
     */

    /**
     * Gets the HandlerList for this event
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
