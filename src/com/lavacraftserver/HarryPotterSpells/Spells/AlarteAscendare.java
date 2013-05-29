package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="AlarteAscendare",
		description="Propels the targeted mob upward",
		range=30,
		goThroughWalls=false
)
public class AlarteAscendare extends Spell implements Listener {
	private static List<Integer> entities = new ArrayList<>();
	
	@Override
	public void cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			le.setVelocity(new Vector(0,1,0));
			if(!AlarteAscendare.entities.contains(le.getEntityId())){
				AlarteAscendare.entities.add(le.getEntityId());
			}
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if(AlarteAscendare.entities.contains(e.getEntity().getEntityId())){
			e.setDamage(0);
			AlarteAscendare.entities.remove(e.getEntity().getEntityId());
		}
	}
	
}
