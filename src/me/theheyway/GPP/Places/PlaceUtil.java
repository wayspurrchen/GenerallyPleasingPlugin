package me.theheyway.GPP.Places;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.theheyway.GPP.Constants;
import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Overlord.Overlord;
import me.theheyway.GPP.Util.SQLUtil;

public class PlaceUtil {
	
	public static final String DB_PLACES_TABLENAME = "Places";
	//TODO: Make places conversion code to better DB
	
	public static final String SPAWNS_FILE_PATH = "plugins/GPP/spawns.data";
	
	public File SPAWNS_FILE = new File(SPAWNS_FILE_PATH);
	public FileConfiguration SPAWNS_FILECONFIG = null;
	
	public static Place getPlace(String name, String type, String world, String owner) {
		String query = "SELECT * FROM " + DB_PLACES_TABLENAME +
				" WHERE type='" + type + "' AND name='" + name + "' AND owner='" + owner + "' AND world='" + world + "'";
		try {
			Connection conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(query);
			rs.next();
			Place place = new Place();
			place.setName(rs.getString(2));
			place.setDescription(rs.getString(3));
			place.setType(rs.getString(4));
			place.setWorld(rs.getString(5));
			place.setOwner(rs.getString(6));
			place.setShape(rs.getString(7));
			place.setDiscoverable(rs.getBoolean(8));
			place.setExpReward(rs.getInt(9));
			place.setSearchable(rs.getBoolean(10));
			place.setPointable(rs.getBoolean(11));
			place.setX1(rs.getDouble(12));
			place.setY1(rs.getDouble(13));
			place.setZ1(rs.getDouble(14));
			place.setX2(rs.getDouble(15));
			place.setY2(rs.getDouble(16));
			place.setZ2(rs.getDouble(17));
			place.setYaw(rs.getDouble(18));
			rs.close();
			conn.close();
			return place;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void loadPlace(Place place, String name, String type, String world, String owner) {
		String query = "SELECT * FROM " + DB_PLACES_TABLENAME +
				" WHERE type='" + type + "' AND name='" + name + "' AND owner='" + owner + "' AND world='" + world + "'";
		try {
			Connection conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(query);
			rs.next();
			place.setName(rs.getString(2));
			place.setDescription(rs.getString(3));
			place.setType(rs.getString(4));
			place.setWorld(rs.getString(5));
			place.setOwner(rs.getString(6));
			place.setShape(rs.getString(7));
			place.setDiscoverable(rs.getBoolean(8));
			place.setExpReward(rs.getInt(9));
			place.setSearchable(rs.getBoolean(10));
			place.setPointable(rs.getBoolean(11));
			place.setX1(rs.getDouble(12));
			place.setY1(rs.getDouble(13));
			place.setZ1(rs.getDouble(14));
			place.setX2(rs.getDouble(15));
			place.setY2(rs.getDouble(16));
			place.setZ2(rs.getDouble(17));
			place.setYaw(rs.getDouble(18));
			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean placeExists(String name, String type, String world, String owner) {
		String query = "SELECT * FROM " + DB_PLACES_TABLENAME +
				" WHERE type='" + type + "' AND name='" + name + "' AND owner='" + owner + "' AND world='" + world + "'";
		try {
			Connection conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(query);
			boolean exists = false;
			if (rs.next()) exists = true;
			rs.close();
			conn.close();
			return exists;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void insertPlace(Place place) throws SQLException {
		int discoverable = place.discoverable ? 1 : 0;
		int searchable = place.searchable ? 1 : 0;;
		int pointable = place.pointable ? 1 : 0;;
		
		String update = "INSERT " + DB_PLACES_TABLENAME + " SET " +
				"name='" + place.getName() + "', " +
				"description='" + place.getDescription() + "', " +
				"type='" + place.getType() + "', " +
				"world='" + place.getWorld() + "', " +
				"owner='" + place.getOwner() + "', " +
				"shape='" + place.getShape() + "', " +
				"discoverable=" + discoverable + ", " +
				"expreward=" + place.getExpReward() + ", " +
				"searchable=" + searchable + ", " +
				"pointable=" + pointable + ", " +
				"x1=" + place.getX1() + ", " +
				"y1=" + place.getY1() + ", " +
				"z1=" + place.getZ1() + ", " +
				"x2=" + place.getX2() + ", " +
				"y2=" + place.getY2() + ", " +
				"z2=" + place.getZ2() + ", " +
				"yaw=" + place.getYaw();
		SQLUtil.transactUpdate(update);
	}
	
	private static void teleportPlayerToBlock(Player executor, Player subject) {
		Block targetBlock = executor.getTargetBlock(null, 70);
		Block aboveBlock = targetBlock.getRelative(BlockFace.UP, 1);
		while (aboveBlock.getType()!=Material.AIR) {
			aboveBlock = aboveBlock.getRelative(BlockFace.UP, 1);
		}
		targetBlock = aboveBlock;
		Location targetLoc = targetBlock.getLocation();
		targetLoc.add(.5, 0, .5); // So we're on top of, and in the center of the block
		targetLoc.setYaw(executor.getLocation().getYaw());
		targetLoc.setPitch(executor.getLocation().getPitch());
		subject.teleport(targetLoc);
	}
	
	private static void teleportPlayerToPlace(Player executor, Player subject, Place place) {
		Location loc = place.generateLocation();
		subject.teleport(loc);
		executor.sendMessage("Player teleported");
	}
	
	/*public static void setHome(Player player) throws SQLException {
		Location loc = player.getLocation();
		int x = (int) loc.getX();
		int y = (int) loc.getY();
		int z = (int) loc.getZ();
		double yaw = loc.getYaw();
		if (placeExists("home", "home", player.getName(), player.getWorld().getName())) {
			String update = "UPDATE " + DB_PLACES_TABLENAME + " SET x=" + x +
					", y=" + y + ", z=" + z + ", yaw=" + yaw +
					" WHERE type='home' AND name='home' AND owner='" + playerName +
					"' AND world='" + player.getWorld().getName() + "'";
			SQLUtil.transactUpdate(update);
			if (caller.getName()!=playerName) {
				caller.sendMessage(ChatColor.YELLOW + "Home location in " + ChatColor.WHITE + caller.getWorld().getName() +
						ChatColor.YELLOW + " for " + ChatColor.WHITE + playerName + ChatColor.YELLOW + " updated.");
			} else caller.sendMessage(ChatColor.YELLOW + "Home location in " + ChatColor.WHITE + caller.getWorld().getName() +
					ChatColor.YELLOW + " updated.");
		} else {
			String update = "INSERT INTO " + DB_PLACES_TABLENAME + 
					" (type, name, owner, world, x, y, z, yaw) VALUES ('home', 'home', '" + playerName
					+ "', '" + caller.getWorld().getName() + "', " + x + ", " + y + ", " + z + ", " + yaw + ")";
			SQLUtil.transactUpdate(update);
			if (caller.getName()!=playerName) {
				caller.sendMessage(ChatColor.YELLOW + "Home location for " + ChatColor.WHITE + playerName + ChatColor.YELLOW +
						" in " + ChatColor.WHITE + caller.getWorld().getName() + ChatColor.YELLOW + " set.");
			} else caller.sendMessage(ChatColor.YELLOW + "Home location in " + ChatColor.WHITE + caller.getWorld().getName() + 
					ChatColor.YELLOW + " set.");
		}
	}
	
	public static boolean setHome(String playerName, int x, int y, int z) throws SQLException {
		Location loc = caller.getLocation();
		int x = (int) loc.getX();
		int y = (int) loc.getY();
		int z = (int) loc.getZ();
		double yaw = loc.getYaw();
		if (placeExists("home", "home", playerName, caller.getWorld().getName())) {
			String update = "UPDATE " + DB_PLACES_TABLENAME + " SET x=" + x +
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
			String update = "INSERT INTO " + DB_PLACES_TABLENAME + 
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
	}*/

}
