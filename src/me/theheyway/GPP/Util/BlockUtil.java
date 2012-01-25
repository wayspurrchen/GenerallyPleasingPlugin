package me.theheyway.GPP.Util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockUtil {
	
	/**
	 * Gets the location of the center of the bottom of a block from the block passed to the function.
	 * 
	 * @param block
	 * @return
	 */
	public static Location getBlockCenter(Block block) {
		Location targetLoc = block.getLocation();
		targetLoc.add(.5, 0, .5); // Exact center of block
		return targetLoc;
	}
	
	
	/**
	 * Gets the location of the center of the bottom of a block from the location passed to the function.
	 * 
	 * @param block
	 * @return
	 */
	public static Location getBlockCenter(Location location) {
		Block locationBlock = location.getBlock();
		Location centerLocation = locationBlock.getLocation();
		centerLocation.add(.5, 0, .5); // Exact center of block
		return centerLocation;
	}
	
	/**
	 * Returns the top block exposed to air above the block given to this function.
	 * 
	 * @param block
	 * @return
	 */
	public static Block getTopBlock(Block block) {
		Block topBlock = block.getRelative(BlockFace.UP, 128-block.getY());
		
		while (topBlock.getType()==Material.AIR) {
			topBlock.getRelative(BlockFace.DOWN);
		}
		
		return topBlock;
	}
	
	/**
	 * Returns the top block exposed to air within the x and z parameters of the Location given to this function.
	 * 
	 * @param location
	 * @return
	 */
	public static Block getTopBlock(Location location) {
		Block locationBlock = location.getBlock().getRelative(BlockFace.UP, 128 - location.getBlockY());
		
		while (locationBlock.getType()==Material.AIR) {
			locationBlock.getRelative(BlockFace.DOWN);
		}
		
		return locationBlock;
	}
	
	/**
	 * Returns the next air block above the current block (usually used to avoid teleporting into solid blocks).
	 * 
	 * @param block
	 * @return
	 */
	public static Block getSurfaceBlock(Block block) {
		Block aboveBlock = block.getRelative(BlockFace.UP);
		while (aboveBlock.getType()!=Material.AIR) {
			aboveBlock = aboveBlock.getRelative(BlockFace.UP);
		}
		return aboveBlock;
	}

}
