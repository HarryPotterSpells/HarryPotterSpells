package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.SpellTargeter;
import com.lavacraftserver.HarryPotterSpells.Utils.SpellTargeter.SpellHitEvent;

@spell (
		name="Incendio",
		description="Lights the targeted mob or block on fire",
		range=50,
		goThroughWalls=false
)
public class Incendio extends Spell {

	public boolean cast(Player p) {
	    SpellTargeter.register(p, new SpellHitEvent() {

            @Override
            public void hitBlock(Block block) {
                Block setOnFire = block.getRelative(BlockFace.UP);
                if(setOnFire.getType().isTransparent())
                    setOnFire.setType(Material.FIRE);
            }

            @Override
            public void hitEntity(LivingEntity entity) {
                entity.setFireTicks(HPS.Plugin.getConfig().getInt("spells.incendio.duration", 100));
            }
	        
	    }, 1.05d, Effect.MOBSPAWNER_FLAMES);
	    return true;
	}
	
}

