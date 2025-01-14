package com.hpspells.core.api.event;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import com.hpspells.core.spell.Spell;

public class SpellHitEvent extends SpellEvent {
    private static final HandlerList handlers = new HandlerList();
    
    private Block block;
    private LivingEntity entity;
    
    public SpellHitEvent(Spell spell, Player caster) {
        this(spell, caster, null, null);
    }
    
    public SpellHitEvent(Spell spell, Player caster, Block block, LivingEntity entity) {
        super(spell, caster);
        this.block = block;
        this.entity = entity;
    }

//    public void onHitBlock(Block block) {
//        this.block = block;
//        this.hitBlock(block);
//    }
//    
//    public void onHitEntity(LivingEntity entity) {
//        this.entity = entity;
//        this.hitEntity(entity);
//    }
//    
//    protected abstract void hitBlock(Block block);
//    
//    protected abstract void hitEntity(LivingEntity entity);
    
    public Block getBlock() {
        return block;
    }
    
    public Entity getEntity() {
        return entity;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    /**
     * Gets the HandlerList for this event
     *
     * @return the handler list
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
