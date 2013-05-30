package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.SpellTargeter;

@spell (
		name="Incendio",
		description="Lights the targeted mob or block on fire",
		range=50,
		goThroughWalls=false
)
public class Incendio extends Spell {

	public void cast(Player p) {
	    SpellTargeter.register(p, Snowball.class, FireworkEffect.builder().withColor(Color.RED, Color.ORANGE).with(Type.BALL).build(), new SpellTargeter.SpellHitEvent() {
            
            @Override
            public void hitEntity(LivingEntity entity) {
                int fireTicks = HPS.Plugin.getConfig().getInt("spells.incendio.duration");
                entity.setFireTicks(fireTicks);
            }
            
            @Override
            public void hitBlock(Block b) {
                Block above = new Location(b.getWorld(), b.getX(), b.getY() + 1, b.getZ()).getBlock();
                if(above.getType() == Material.AIR)
                    above.setType(Material.FIRE);
            }
        });
	}
	
}

