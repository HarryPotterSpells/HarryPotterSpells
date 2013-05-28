package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HarryPotterSpells;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Time",
		description="Toggles the time depending on your target block",
		range=0,
		goThroughWalls=false
)
public class TimeSpell extends Spell{
	
	//Dawn == 0L
	//Morning == 2500L
	//Noon == 6000L
	//Evening == 11000L
	//Night == 15000L
	public void cast(Player p) {
		World w = p.getWorld();
		Material m = p.getTargetBlock(null, 50).getType();
		if(m == Material.GLOWSTONE) {
			w.setTime(0L);
		} else if(m == Material.OBSIDIAN) {
			w.setTime(15000L);
		} else {
			long time = w.getTime();
			if(time < 12000) {
				time = time + 12000L;
			} else {
				time = time - 12000L;
			}
			if(HarryPotterSpells.Plugin.getConfig().getBoolean("spells.time.lightning", true))
				awesomeLightning(p.getLocation(), w);
			w.setTime(time);
		}
	}

	public void awesomeLightning(Location l, World w) {
		double x = l.getX(), y = l.getY(), z = l.getZ();
		//X Y Z
		w.strikeLightningEffect(new Location(w, x, y, z - 2));
		w.strikeLightningEffect(new Location(w, x, y, z + 2));
		w.strikeLightningEffect(new Location(w, x - 2, y, z));
		w.strikeLightningEffect(new Location(w, x + 2, y, z));
	}
	
}
