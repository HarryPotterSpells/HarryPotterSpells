package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.material.Chest;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Glacius",
		description="Encases the target in ice",
		range=50,
		goThroughWalls=false,
		cooldown=120
)
public class Glacius extends Spell {
	HashMap<Location, Material> blocks = new HashMap<Location, Material>();
	private final BlockFace[] surroundings = new BlockFace[]{
															BlockFace.NORTH, 
												            BlockFace.NORTH_EAST,
												            BlockFace.EAST,
												            BlockFace.SOUTH_EAST,
												            BlockFace.SOUTH,
												            BlockFace.SOUTH_WEST,
												            BlockFace.WEST,
												            BlockFace.NORTH_WEST
												            };
	public Glacius(HPS plugin) {
	    super(plugin);
	}
	
	@Override
	public boolean cast(Player p) {
		if(Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls()) instanceof Player) {
			Player player = (Player) Targeter.getTarget(p, this.getRange(), this.canBeCastThroughWalls());
			
			World world = player.getWorld();
			double playerX = Math.round(player.getLocation().getBlockX() - .5) + .5;
			double playerY = player.getLocation().getBlockY();
			double playerZ = Math.round(player.getLocation().getBlockZ() - .5) + .5;
			float pitch = player.getLocation().getPitch();
			float yaw = player.getLocation().getYaw();
			
			Location loc = new Location(world, playerX, playerY, playerZ, pitch, yaw);
			player.teleport(loc);
			
			final Location[] locations = new Location[]{
											player.getLocation(),
											player.getLocation().add(0,1,0),
											player.getLocation().add(0,2,0)
											};
			
			final Location top = player.getLocation().add(0,2,0);
			blocks.put(top, top.getBlock().getType());
			top.getBlock().setType(Material.ICE);
			
			for (Location locs : locations) {
				for (BlockFace bf : surroundings) {
					
					final Block relative = locs.getBlock().getRelative(bf, 1);
					
					blocks.put(relative.getLocation(), relative.getType());
					relative.setType(Material.ICE);
	
				}
				
			}
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {
				public void run() {
					
					for (Location locs : locations) {
						for (BlockFace bf : surroundings) {
							final Block relative = locs.getBlock().getRelative(bf, 1);
							
							relative.setType(blocks.get(relative.getLocation()));
							blocks.remove(relative.getLocation());
							
						}
						
					}
					
					top.getBlock().setType(blocks.get(top));
					blocks.remove(top);

				}
				
			}, HPS.getConfig().getLong("spells.glacius.ice-duration", 40L));
			
			return true;
			
		} else {
			HPS.PM.warn(p, "This can only be used on a player.");
			return false;
		}
	}
}
