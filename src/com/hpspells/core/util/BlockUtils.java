package com.hpspells.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Door.Hinge;

/**
 * Class containing block related helpful methods.
 * 
 * @author jacklin213
 *
 */
public final class BlockUtils {
	
	// Create a door mapper for different hinges. TODO: See if one side is enough and just do getOppositeFace()
	@SuppressWarnings("serial")
	private static Map<Hinge, Map<BlockFace, BlockFace>> doorMapper = new HashMap<Hinge, Map<BlockFace, BlockFace>>(){{
		Map<BlockFace, BlockFace> leftFaceMapper = new HashMap<BlockFace, BlockFace>() {{
			put(BlockFace.NORTH, BlockFace.EAST);
			put(BlockFace.SOUTH, BlockFace.WEST);
			put(BlockFace.EAST, BlockFace.SOUTH);
			put(BlockFace.WEST, BlockFace.NORTH);
		}};
		Map<BlockFace, BlockFace> rightFaceMapper = new HashMap<BlockFace, BlockFace>() {{
			put(BlockFace.NORTH, BlockFace.WEST);
			put(BlockFace.SOUTH, BlockFace.EAST);
			put(BlockFace.EAST, BlockFace.NORTH);
			put(BlockFace.WEST, BlockFace.SOUTH);
		}};
		put(Hinge.LEFT, leftFaceMapper);
		put(Hinge.RIGHT, rightFaceMapper);
	}};
	
	/**
     * Find a block that is adjacent to another block given a Material.
     *
     * @param block Source block
     * @param material Material to look for
     * @param ignore Blocks to ignore
     * @return Matching block found
     */
	public static Block findAdjacentBlock(Block block, Material material, Block... ignore) {
        BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
        List<Block> ignoreList = Arrays.asList(ignore);

        for (BlockFace face : faces) {
            Block adjacentBlock = block.getRelative(face);

            if (adjacentBlock.getType() == material && !ignoreList.contains(adjacentBlock)) {
                return adjacentBlock;
            }
        }

        return null;
    }
	
	/**
     * Find a block that is adjacent to another block on any of the block's 6 sides given a Material.
     *
     * @param block Source block
     * @param material Material to look for
     * @param ignore Blocks to ignore
     * @return Matching block found
     */
    public Block findAdjacentBlockOnAllSides(Block block, Material material, Block... ignore) {
        BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
        List<Block> ignoreList = Arrays.asList(ignore);

        for (BlockFace face : faces) {
            Block adjacentBlock = block.getRelative(face);

            if (adjacentBlock.getType() == material && !ignoreList.contains(adjacentBlock)) {
                return adjacentBlock;
            }
        }

        return null;
    }
	
    /**
     * Find all the blocks that are adjacent to another block on any of the block's 6 sides given a Material.
     * 
     * @param block Source block
     * @param material Material to look for
     * @param ignore Blocks to ignore
     * @return A list of matching blocks found
     */
	public static List<Block> findAllAdjacentBlocks(Block block, Material material, Block... ignore) {
        BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
        List<Block> ignoreList = Arrays.asList(ignore);
        List<Block> adjacentBlockList = new ArrayList<>();

        for (BlockFace face : faces) {
            Block adjacentBlock = block.getRelative(face);

            if (adjacentBlock.getType() == material && !ignoreList.contains(adjacentBlock)) {
            	adjacentBlockList.add(adjacentBlock);
            }
        }

        return adjacentBlockList;
    }	
	
	/**
	 * If the door is a double door and a valid door block is given, will return the corresponding other door block.
	 * 
	 * @param block Door block
	 * @return other door block if it exists otherwise null
	 */
	public static Block getOtherDoorBlock(Block block) {
		Block sideBlock = null;
		if (Tag.DOORS.isTagged(block.getType())) {
			Door door = (Door) block.getBlockData();
			Hinge hinge = door.getHinge();
			BlockFace face = doorMapper.get(hinge).get(door.getFacing());
			sideBlock = block.getRelative(face);
			if (sideBlock != null) {
				if (Tag.DOORS.isTagged(sideBlock.getType())) {
					Door otherDoor = (Door) sideBlock.getBlockData();
					if (door.getFacing() == otherDoor.getFacing()) {
						// if the 2 doors have different facing hinges
						if ((hinge == Hinge.LEFT && otherDoor.getHinge() == Hinge.RIGHT) || (hinge == Hinge.RIGHT && otherDoor.getHinge() == Hinge.LEFT)) {
							return sideBlock;
						}
					}
				}
			}
		}
		return null;
	}

}
