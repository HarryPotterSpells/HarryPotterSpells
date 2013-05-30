package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.util.BlockIterator;

/**
 * A targeter class that gets the target of a spell
 * @deprecated Use {@link SpellTargeter}
 */
@Deprecated
public class Targeter {
	
	public static HashSet<Byte> getTransparentBlocks() {
		HashSet<Byte> b = new HashSet<Byte>();
		b.add((byte) 0);
		b.add((byte) 8);
		b.add((byte) 9);
		b.add((byte) 10);
		b.add((byte) 11);
		return b;
	}
	
	public static LivingEntity getTarget(Player p, int range, boolean goThroughWalls) {
		List<Entity> nearbyE = p.getNearbyEntities(range, range, range);
	    ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();
	    for (Entity e : nearbyE) {
	        if (e instanceof LivingEntity) {
	          	livingE.add((LivingEntity) e);
	        }
	    }

	    LivingEntity target = null;
	    BlockIterator bItr;
	    try {
	      	bItr = new BlockIterator(p, range);
	    } catch (IllegalStateException e) {
			return null;
	    }
	        
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
                float cY = getYMod(e);
                float cV = 1.2f;
                if ((bx-cV <= ex && ex <= bx+cV) && (bz-cV <= ez && ez <= bz+cV) && (by-cY <= ey && ey <= by+0.1)) {
                   	target = e;
                    return target;
                }
                if(goThroughWalls && !getTransparentBlocks().contains(block.getTypeId()))
                	return null;
            }
        }
        return null;
	}

	private static float getYMod (Entity e) {
		float y;
        if(e instanceof Enderman) {
        	y = 2.1f;
        }else if(e instanceof Cow || e instanceof Pig || e instanceof Sheep || e instanceof MushroomCow || e instanceof MagmaCube || e instanceof Slime) {
        	y = 1.1f;
        }else if(e instanceof Ocelot || e instanceof Wolf || e instanceof Chicken || e instanceof CaveSpider || e instanceof Spider ||  e instanceof Silverfish || e instanceof Squid){
        	y = 0.1f;
        }else if(e instanceof Player || e instanceof Skeleton || e instanceof Creeper || e instanceof Zombie || e instanceof PigZombie ||e instanceof Snowman ||  e instanceof Villager || e instanceof Blaze) {
        	y = 1.1f;
        }else{
        	y = 1.0f;
        }
        return y;
	}
}