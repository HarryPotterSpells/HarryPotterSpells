package com.lavacraftserver.HarryPotterSpells.Spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell (
		name="Glacius",
		description="Encases the target in ice",
		range=50,
		goThroughWalls=false
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
	public void cast(Player player) {
		Location[] locs = new Location[]{
										player.getLocation(),
										player.getLocation().add(0,1,0),
										player.getLocation().add(0,2,0)
										};
		for(Location loc : locs) {
			for(BlockFace bf : surroundings){
				loc.getBlock().getRelative(bf, 1).setType(Material.ICE);
			}
		}
		player.getLocation().add(0,2,0).getBlock().setType(Material.ICE);
	}
}
