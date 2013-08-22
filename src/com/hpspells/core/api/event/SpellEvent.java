package com.hpspells.core.api.event;

import com.hpspells.core.spell.Spell;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Generic event for spells
 */
public class SpellEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Spell spell;
    private Player caster;

    public SpellEvent(Spell spell, Player caster) {
        this.spell = spell;
        this.caster = caster;
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
     * Gets the spell that is being cast
     *
     * @return the spell
     */
    public Spell getSpell() {
        return spell;
    }

    /**
     * Gets the caster of the spell
     *
     * @return the caster
     */
    public Player getCaster() {
        return caster;
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
