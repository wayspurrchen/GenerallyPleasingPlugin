package me.theheyway.GPP.Util;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.theheyway.GPP.Constants;
import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Overlord.Overlord;

public class PlaceUtil {
	
	public static final String DB_PLACES_TABLENAME = "Places";
	//TODO: Make places conversion code to better DB
	
	public static final String SPAWNS_FILE_PATH = "plugins/GPP/spawns.data";
	
	public File SPAWNS_FILE = new File(SPAWNS_FILE_PATH);
	public FileConfiguration SPAWNS_FILECONFIG = null;
	
	public static double[] getPlace(String type, String name, String owner, String world) {
		String query = "SELECT x,y,z,yaw FROM " + DB_PLACES_TABLENAME +
				" WHERE type='" + type + "' AND name='" + name + "' AND owner='" + owner + "' AND world='" + world + "'";
		try {
			Connection conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(query);
			rs.next();
			double xyzyaw[] = new double[4];
			xyzyaw[0] = rs.getDouble(1);
			xyzyaw[1] = rs.getDouble(2);
			xyzyaw[2] = rs.getDouble(3);
			xyzyaw[3] = rs.getDouble(4);
			rs.close();
			conn.close();
			return xyzyaw;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean placeExists(String type, String name, String owner, String world) {
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
	
	/**
	 * Puts Player inside 
	 * 
	 * @param player
	 * @return
	 * @throws SQLException
	 */
	public static boolean setHome(Player player) throws SQLException {
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
	}

}
