package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.util.BlockIterator;

import com.lavacraftserver.HarryPotterSpells.PM;

public class Multicorfors {
	
	public static void cast(Player p) {
		final Block b = p.getTargetBlock(transparentBlocks(), 25);
		if(getTarget(p) instanceof Sheep) {
			final Sheep sheep = (Sheep)getTarget(p);
			sheep.getWorld().createExplosion(sheep.getLocation(), 0F);
			PM.hps.getServer().getScheduler().scheduleSyncDelayedTask(PM.hps, new Runnable() {
				   public void run() {
					   sheep.setColor(randomDyeColor());
				   }
				}, 6L);
		} else if(b.getType() == Material.WOOL) {
			p.getWorld().createExplosion(b.getLocation(), 0F);
			PM.hps.getServer().getScheduler().scheduleSyncDelayedTask(PM.hps, new Runnable() {
				   public void run() {
					   b.setData(randomDyeColorInt());
				   }
				}, 6L);
		} else {
			PM.warn(p, "You can only cast this spell on Sheep or Wool.");
		}
	}
	
	public static LivingEntity getTarget(Player p) {
		List<Entity> nearbyE = p.getNearbyEntities(25, 25, 25);
        ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();
        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity) {
            	livingE.add((Sheep) e);
            }
        }
        LivingEntity target = null;
        BlockIterator bItr = new BlockIterator(p, 25);
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
	
	public static DyeColor randomDyeColor() {
		DyeColor et;
		int maxMobs = 16, minMobs = 1;
		int randomNum = new Random().nextInt(maxMobs - minMobs + 1) + minMobs;
		
		switch(randomNum) {
		case 1:	et = DyeColor.BLACK;
				break;
		case 2:	et = DyeColor.BLUE;
				break;
		case 3: et = DyeColor.BROWN;
				break;
		case 4:	et = DyeColor.CYAN;
				break;
		case 5:	et = DyeColor.GRAY;
				break;
		case 6:	et = DyeColor.GREEN;
				break;
		case 7: et = DyeColor.LIGHT_BLUE;
				break;
		case 8:	et = DyeColor.LIME;
				break;
		case 9:	et = DyeColor.MAGENTA;
				break;
	    case 10: et = DyeColor.ORANGE;
				 break;
		case 11: et = DyeColor.PINK;
				 break;
		case 12: et = DyeColor.PURPLE;
				 break;
		case 13: et = DyeColor.RED;
				 break;
		case 14: et = DyeColor.SILVER;
				 break;
		case 15: et = DyeColor.WHITE;
				 break;
		case 16: et = DyeColor.YELLOW;
				 break;
		default: et = DyeColor.WHITE;
				 break;
		}
		return et;
	}
	
	public static byte randomDyeColorInt() {
		int maxMobs = 16, minMobs = 1;
		int randomNum = new Random().nextInt(maxMobs - minMobs + 1) + minMobs;
		return (byte)randomNum;
	}
	
	public static HashSet<Byte> transparentBlocks() {
		HashSet<Byte> b = new HashSet<Byte>();
		b.add((byte) 0);
		b.add((byte) 8);
		b.add((byte) 9);
		b.add((byte) 10);
		b.add((byte) 11);
		return b;
	}

}
