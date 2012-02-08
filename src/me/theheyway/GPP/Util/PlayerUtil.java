package me.theheyway.GPP.Util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlayerUtil {
    
	/**
	 * Heals a player for a specified amount. Can use negative values.
	 * 
	 * @param player
	 * @param amount
	 */
    public static void heal(Player player, int amount) {
    	int healAmount = amount;
		int health = player.getHealth() + healAmount;
		
		//validations
		if (health > player.getMaxHealth())
			health = player.getMaxHealth();
		else if (health < 0)
			health = 0;
		
		//apply changes
		player.setHealth(health); //YES! Totally mean you can /heal -3;
    }
    
    /**
	 * Teleports a player to the block passed to this function, placing the player in the center while retaining the player's
	 * yaw and pitch. This will put a player at the bottom and center of a block, regardless of its data type. If you want to
	 * enable a player to look at a block and teleport to its top, use teleportPlayerToTopOfBlock.
	 * 
	 * @param subject
	 * @param block
	 */
	public static void teleportPlayerToBlock(Player subject, Block block) {
		Location targetLoc = BlockUtil.getBlockCenter(block);
		targetLoc.setYaw(subject.getLocation().getYaw());
		targetLoc.setPitch(subject.getLocation().getPitch());
		subject.teleport(targetLoc);
	}
	
	/**
	 * Teleports a player on top of the block passed to this function, placing the player in the center while retaining the player's
	 * yaw and pitch.
	 * 
	 * @param subject
	 * @param block
	 */
	public static void teleportPlayerToTopOfBlock(Player subject, Block block) {
		Location targetLoc = BlockUtil.getBlockCenter(BlockUtil.getTopBlock(block));
		targetLoc.setYaw(subject.getLocation().getYaw());
		targetLoc.setPitch(subject.getLocation().getPitch());
		subject.teleport(targetLoc);
	}

}
