package com.hpspells.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * Class containing block related helpful methods.
 * 
 * @author jacklin213
 *
 */
public final class BlockUtils {
	
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


}
