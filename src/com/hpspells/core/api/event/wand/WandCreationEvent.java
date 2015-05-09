package com.hpspells.core.api.event.wand;

import com.hpspells.core.Wand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


/**
 * An event called when {@link com.hpspells.core.Wand} creates a new wand
 */
public class WandCreationEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private Player owner;
    private Wand.Lore lore;
    private boolean enchantmentEffect;

    public WandCreationEvent(Player owner, Wand.Lore lore, boolean enchantmentEffect) {
        this.owner = owner;
        this.lore = lore;
        this.enchantmentEffect = enchantmentEffect;
    }

    /**
     * Checks if the wand being created has an owner
     *
     * @return {@code true} if the wand has an owner
     */
    public boolean hasOwner() {
        return owner != null;
    }

    /**
     * Gets the owner of the wand. This can be null if there is no owner.
     *
     * @return the owner of the wand
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Checks if the wand being created has any lore
     *
     * @return {@code true} if the wand has lore
     */
    public boolean hasLore() {
        return lore != null;
    }

    /**
     * Gets the lore of the wand. This can be null if there is no lore.
     *
     * @return the lore of the wand
     */
    public Wand.Lore getLore() {
        return lore;
    }

    /**
     * Sets the lore of the wand. Can be {@code null} to remove all lore.
     *
     * @param lore the desired lore
     */
    public void setLore(Wand.Lore lore) {
        this.lore = lore;
    }

    /**
     * Checks if the wand has the enchantment effect
     *
     * @return {@code true} if the wand has an enchantment effect
     */
    public boolean hasEnchantmentEffect() {
        return enchantmentEffect;
    }

    /**
     * Sets the status of the wand's enchantment effect
     *
     * @param status {@code true} to give the wand an enchantment effect
     */
    public void setEnchantmentEffect(boolean status) {
        enchantmentEffect = status;
    }

    /**
     * Gets the HandlerList for this event
     *
     * @return the handler list
     */
    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
