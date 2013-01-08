package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Expelliarmus",
		description="Disarms your target",
		range=25,
		goThroughWalls=false
)
public class Expelliarmus extends Spell {
	
	public Expelliarmus(HarryPotterSpells instance) {
		super(instance);
	}

	public void cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			Player target = (Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			
			Location targetloc = target.getLocation();
			//We need to get the direction the target is facing, add 2, and drop it there.
			
			List<Integer> disarmItems = Arrays.asList({Material.STICK.getId(), Material.WOOD_SWORD.getId(),
				Material.STONE_SWORD.getId(), Material.IRON_SWORD.getId(), Material.GOLD_SWORD.getId(),
				Material.DIAMOND_SWORD.getId(), Material.BOW.getId()}
			
			if (disarmItems.contains(target.getItemInHand().getTypeId())) {
				Item i = target.getWorld().dropItem(targetloc, target.getItemInHand());
				target.setItemInHand(null);
				Vector vector = targetloc.getDirection();
				i.setVelocity(new Vector
						(vector.getX() * 2,
						vector.getY() * 2,
						vector.getZ() * 2));
			}
			
		} else {
			plugin.PM.warn(p, "You can only cast this on players.");
		}
	}
}
