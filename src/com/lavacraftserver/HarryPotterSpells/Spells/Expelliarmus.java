package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Expelliarmus",
		description="Disarms your target",
		range=25,
		goThroughWalls=false,
		cooldown=120
)
public class Expelliarmus extends Spell {

	public boolean cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			Player target = (Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			Location targetloc = target.getLocation();			
			List<Integer> disarmItems = Arrays.asList(HPS.Wand.getWand().getTypeId());
			if((Boolean) getConfig("disarm-weapons", true))
				disarmItems.addAll(Arrays.asList(Material.STICK.getId(), Material.WOOD_SWORD.getId(), Material.STONE_SWORD.getId(), Material.IRON_SWORD.getId(), Material.GOLD_SWORD.getId(), Material.DIAMOND_SWORD.getId(), Material.BOW.getId()));

			if (disarmItems.contains(target.getItemInHand().getTypeId())) {
				Item i = target.getWorld().dropItem(targetloc, target.getItemInHand());
				target.setItemInHand(null);
				Vector vector = targetloc.getDirection();
				i.setVelocity(new Vector(vector.getX() * 2, vector.getY() * 2, vector.getZ() * 2));
			}
			
			return true;
		} else {
			HPS.PM.warn(p, "This can only be used on a player.");
			return false;
		}
	}
}
