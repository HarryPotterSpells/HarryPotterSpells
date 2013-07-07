package com.hpspells.core.spell;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.Targeter;

@SpellInfo (
		name="Expelliarmus",
		description="descExpelliarmus",
		range=25,
		goThroughWalls=false,
		cooldown=120
)
public class Expelliarmus extends Spell {
    
    public Expelliarmus(HPS instance) {
        super(instance);
    }

    public boolean cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) != null) {
			LivingEntity target = Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			Location targetloc = target.getLocation();			
			List<Integer> disarmItems = Arrays.asList(HPS.Wand.getWand().getTypeId());
			if((Boolean) getConfig("disarm-weapons", true))
				disarmItems.addAll(Arrays.asList(Material.STICK.getId(), Material.WOOD_SWORD.getId(), Material.STONE_SWORD.getId(), Material.IRON_SWORD.getId(), Material.GOLD_SWORD.getId(), Material.DIAMOND_SWORD.getId(), Material.BOW.getId()));

			if (disarmItems.contains(target.getEquipment().getItemInHand().getTypeId())) {
				Item i = target.getWorld().dropItem(targetloc, target.getEquipment().getItemInHand());
				target.getEquipment().setItemInHand(null);
				Vector vector = targetloc.getDirection();
				i.setVelocity(new Vector(vector.getX() * 2, vector.getY() * 2, vector.getZ() * 2));
			}
			
			return true;
		} else {
			HPS.PM.warn(p, HPS.Localisation.getTranslation("spellPlayerOnly"));
			return false;
		}
	}
}
