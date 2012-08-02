package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.lavacraftserver.HarryPotterSpells.Spell;
import com.lavacraftserver.HarryPotterSpells.Spells.Confundo;

public class Targeter extends JavaPlugin implements Listener {
	public static Map<UUID, Spell> NeedsTargeted = new HashMap<UUID, Spell>();
	
	@EventHandler
	public void onEntityDamageEntityEvent(EntityDamageByEntityEvent e) {
		Entity victim = e.getEntity();
		Entity attacker = e.getDamager();
		if(NeedsTargeted.containsKey(attacker.getUniqueId())) {
			Spell spell = NeedsTargeted.get(attacker.getUniqueId());
			if(spell == Spell.CONFUNDO) {Confundo.hit(victim);}
		}
	}

}
