package com.hpspells.core.api.event;

import com.hpspells.core.api.SpellBookRecipe;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event called just before a {@link SpellBookRecipe} is added to the server
 */
public class SpellBookRecipeAddEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private SpellBookRecipe recipe;
    private boolean cancelled = false;

    public SpellBookRecipeAddEvent(SpellBookRecipe recipe) {
        this.recipe = recipe;
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
     * Gets the cancellation status of this spell
     *
     * @return {@code true} if the spell has been cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation status of this spell
     *
     * @param cancel
     */
    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    /**
     * Gets the recipe associated with this event
     *
     * @return the recipe
     */
    public SpellBookRecipe getRecipe() {
        return recipe;
    }

    /**
     * Sets the recipe associated with this event
     *
     * @param recipe
     */
    public void setRecipe(SpellBookRecipe recipe) {
        this.recipe = recipe;
    }

    /*
     * START STATIC METHODS
     */

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
