package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;
import com.lavacraftserver.HarryPotterSpells.Utils.Targeter;

@spell (
		name="Glacius",
		description="Encases the target in ice",
		range=50,
		goThroughWalls=false,
		cooldown=60
)
public class Glacius extends Spell {
	
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
			
			Location[] locations = new Location[]{
											player.getLocation(),
											player.getLocation().add(0,1,0),
											player.getLocation().add(0,2,0)
											};
			for(Location locs : locations) {
				for(BlockFace bf : surroundings){
					locs.getBlock().getRelative(bf, 1).setType(Material.ICE);
				}
			}
			player.getLocation().add(0,2,0).getBlock().setType(Material.ICE);
			return true;
		} else {
			HPS.PM.warn(p, "This can only be used on a player.");
			return false;
		}
	}
}
