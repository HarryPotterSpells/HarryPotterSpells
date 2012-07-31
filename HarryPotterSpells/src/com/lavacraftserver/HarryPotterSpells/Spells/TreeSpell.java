package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TreeSpell {

	public static void cast(Player p) {
		Block block = p.getTargetBlock(tranlist(), 50);
		if (block.getType() == Material.GRASS || block.getType() == Material.DIRT) {
			p.getWorld().generateTree(block.getLocation(), TreeType.BIG_TREE);
			boom(block, block.getWorld());
		}
	}
	
	public static void boom (Block block, World world) {
		List <Block> blocks = new LinkedList <Block>();
		Block highest = getHighestLog(block);
		getBlocksToChop(block, highest, blocks);
		explode(block, blocks, world);
		return;
	}
	
	public static Block getHighestLog(Block block) {
		while (block.getRelative(BlockFace.UP).getType() == Material.LOG) {
			block = block.getRelative(BlockFace.UP);
		}
		return block;
	}
	
	public static void getBlocksToChop (Block block, Block highest, List <Block> blocks) {
		while (block.getY() <= highest.getY()) {
			if (!blocks.contains(block)) {
				blocks.add(block);
			}
			getBranches(block, blocks, block.getRelative(BlockFace.NORTH));
			getBranches(block, blocks, block.getRelative(BlockFace.NORTH_EAST));
			getBranches(block, blocks, block.getRelative(BlockFace.EAST));
			getBranches(block, blocks, block.getRelative(BlockFace.SOUTH_EAST));
			getBranches(block, blocks, block.getRelative(BlockFace.SOUTH));
			getBranches(block, blocks, block.getRelative(BlockFace.SOUTH_WEST));
			getBranches(block, blocks, block.getRelative(BlockFace.WEST));
			getBranches(block, blocks, block.getRelative(BlockFace.NORTH_WEST));
			if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH))) {
				getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH));
			}
			if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST))) {
				getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_EAST));
			}
			if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST))) {
				getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST));
			}
			if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST))) {
				getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_EAST));
			}
			if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH))) {
				getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH));
			}
			if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST))) {
				getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH_WEST));
			}
			if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST))) {
				getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST));
			}
			if (!blocks.contains(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST))) {
				getBranches(block, blocks, block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH_WEST));
			}
			if (!blocks.contains(block.getRelative(BlockFace.UP)) && block.getRelative(BlockFace.UP).getType() == Material.LOG) {
				block = block.getRelative(BlockFace.UP);
			} else break;
		}
	}
	
	public static void getBranches(Block block, List<Block> blocks, Block other) {
		if (!blocks.contains(other) && other.getType() == Material.LOG) {
			getBlocksToChop(other, getHighestLog(other), blocks);
		}
	}
	
	public static void explode (Block block, List<Block> blocks, World world) {
		for (int counter = 0; counter < blocks.size(); counter++) {
			block = blocks.get(counter);
			world.createExplosion(block.getLocation(), 0);
		}
	}
	
	public static HashSet<Byte> tranlist() {
		HashSet<Byte> list = new HashSet<Byte>();
		list.add((byte)0);
		list.add((byte)8);
		list.add((byte)9);
		list.add((byte)10);
		list.add((byte)11);
		list.add((byte)31);
		list.add((byte)32);
		list.add((byte)37);
		list.add((byte)38);
		list.add((byte)39);
		list.add((byte)40);
		list.add((byte)106);
		return list;
	}
}

