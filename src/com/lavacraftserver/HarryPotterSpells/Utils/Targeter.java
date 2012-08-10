package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;

public class Targeter extends JavaPlugin {
	
	public static LivingEntity getTarget(Player p, int range) {
		List<Entity> nearbyE = p.getNearbyEntities(range, range, range);
        ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();
        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity) {
            	livingE.add((LivingEntity) e);
            }
        }
        LivingEntity target = null;
        BlockIterator bItr = new BlockIterator(p, range);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        while (bItr.hasNext()) {
                block = bItr.next();
                bx = block.getX();
                by = block.getY();
                bz = block.getZ();
                for (LivingEntity e : livingE) {
                    loc = e.getLocation();
                    ex = loc.getX();
                    ey = loc.getY();
                    ez = loc.getZ();
                    if ((bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) {
                    	target = e;
                        break;
                    }
                }
        }
        return target;
	}

}
