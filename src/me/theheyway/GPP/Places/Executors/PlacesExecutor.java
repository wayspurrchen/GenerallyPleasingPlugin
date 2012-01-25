package me.theheyway.GPP.Places.Executors;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;

import me.theheyway.GPP.Executor;
import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Overlord.Cruelty;
import me.theheyway.GPP.Overlord.Overlord;
import me.theheyway.GPP.Util.CommandUtil;
import me.theheyway.GPP.Util.PlaceUtil;
import me.theheyway.GPP.Util.SQLUtil;
import me.theheyway.GPP.Util.GenUtil;
import me.theheyway.GPP.Util.MiscUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public class PlacesExecutor extends Executor {
	
	private static void teleportToBlock(Player player, Player subject) {
		Block targetBlock = player.getTargetBlock(null, 70);
		Block aboveBlock = targetBlock.getRelative(BlockFace.UP, 1);
		while (aboveBlock.getType()!=Material.AIR) {
			aboveBlock = aboveBlock.getRelative(BlockFace.UP, 1);
		}
		targetBlock = aboveBlock;
		Location targetLoc = targetBlock.getLocation();
		targetLoc.add(.5, 0, .5); // So we're on top of, and in the center of the block
		targetLoc.setYaw(player.getLocation().getYaw());
		targetLoc.setPitch(player.getLocation().getPitch());
		subject.teleport(targetLoc);
	}
	
	private static boolean setHome(Player caller, String playerName) throws SQLException {
		Location loc = caller.getLocation();
		int x = (int) loc.getX();
		int y = (int) loc.getY();
		int z = (int) loc.getZ();
		double yaw = loc.getYaw();
		if (PlaceUtil.locationExists("home", "home", playerName, caller.getWorld().getName())) {
			String update = "UPDATE " + DB_LOCATIONS_TABLENAME + " SET x=" + x +
					", y=" + y + ", z=" + z + ", yaw=" + yaw +
					" WHERE type='home' AND name='home' AND owner='" + playerName +
					"' AND world='" + caller.getWorld().getName() + "'";
			SQLUtil.transactUpdate(update);
			if (caller.getName()!=playerName) {
				caller.sendMessage(ChatColor.YELLOW + "Home location in " + ChatColor.WHITE + caller.getWorld().getName() +
						ChatColor.YELLOW + " for " + ChatColor.WHITE + playerName + ChatColor.YELLOW + " updated.");
			} else caller.sendMessage(ChatColor.YELLOW + "Home location in " + ChatColor.WHITE + caller.getWorld().getName() +
					ChatColor.YELLOW + " updated.");
			return true;
		} else {
			String update = "INSERT INTO " + DB_LOCATIONS_TABLENAME + 
					" (type, name, owner, world, x, y, z, yaw) VALUES ('home', 'home', '" + playerName
					+ "', '" + caller.getWorld().getName() + "', " + x + ", " + y + ", " + z + ", " + yaw + ")";
			SQLUtil.transactUpdate(update);
			if (caller.getName()!=playerName) {
				caller.sendMessage(ChatColor.YELLOW + "Home location for " + ChatColor.WHITE + playerName + ChatColor.YELLOW +
						" in " + ChatColor.WHITE + caller.getWorld().getName() + ChatColor.YELLOW + " set.");
			} else caller.sendMessage(ChatColor.YELLOW + "Home location in " + ChatColor.WHITE + caller.getWorld().getName() + 
					ChatColor.YELLOW + " set.");
			return true;
		}
	}
	

}
