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
import me.theheyway.GPP.Util.BlockUtil;
import me.theheyway.GPP.Util.SQLUtil;

public class PlaceUtil {
	
	public static final String DB_PLACES_TABLENAME = "Places";
	//TODO: Make places conversion code to better DB
	
	public static final String SPAWNS_FILE_PATH = "plugins/GPP/spawns.data";
	
	public File SPAWNS_FILE = new File(SPAWNS_FILE_PATH);
	public FileConfiguration SPAWNS_FILECONFIG = null;
	
	/**
	 * Returns a Place object with the information from the Places database
	 * 
	 * @param name
	 * @param type
	 * @param world
	 * @param owner
	 * @return
	 */
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
	
	/**
	 * Returns a Place object with the minimal information from the Places database required to give it teleportation functionality.
	 * 
	 * @param place
	 * @param name
	 * @param type
	 * @param world
	 * @param owner
	 */
	public static Place getMinimalPlace(String name, String type, String world, String owner) {
		String query = "SELECT name,type,world,owner,shape,x1,y1,z1,x2,y2,z2,yaw FROM " + DB_PLACES_TABLENAME +
				" WHERE type='" + type + "' AND name='" + name + "' AND owner='" + owner + "' AND world='" + world + "'";
		try {
			Connection conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(query);
			rs.next();
			Place place = new Place();
			place.setName(rs.getString(1));
			place.setType(rs.getString(2));
			place.setWorld(rs.getString(3));
			place.setOwner(rs.getString(4));
			place.setShape(rs.getString(5));
			place.setX1(rs.getDouble(6));
			place.setY1(rs.getDouble(7));
			place.setZ1(rs.getDouble(8));
			place.setX2(rs.getDouble(9));
			place.setY2(rs.getDouble(10));
			place.setZ2(rs.getDouble(11));
			place.setYaw(rs.getDouble(12));
			rs.close();
			conn.close();
			return place;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Loads the passed Place object with place data from the database.
	 * 
	 * @param place
	 * @param name
	 * @param type
	 * @param world
	 * @param owner
	 */
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
	
	/**
	 * Loads the passed Place object with only information used for teleportation. Less intensive.
	 * 
	 * @param place
	 * @param name
	 * @param type
	 * @param world
	 * @param owner
	 */
	public static void loadMinimalPlace(Place place, String name, String type, String world, String owner) {
		String query = "SELECT name,type,world,owner,shape,x1,y1,z1,x2,y2,z2,yaw FROM " + DB_PLACES_TABLENAME +
				" WHERE type='" + type + "' AND name='" + name + "' AND owner='" + owner + "' AND world='" + world + "'";
		try {
			Connection conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(query);
			rs.next();
			place.setName(rs.getString(1));
			place.setType(rs.getString(2));
			place.setWorld(rs.getString(3));
			place.setOwner(rs.getString(4));
			place.setShape(rs.getString(5));
			place.setX1(rs.getDouble(6));
			place.setY1(rs.getDouble(7));
			place.setZ1(rs.getDouble(8));
			place.setX2(rs.getDouble(9));
			place.setY2(rs.getDouble(10));
			place.setZ2(rs.getDouble(11));
			place.setYaw(rs.getDouble(12));
			rs.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Checks if a place exists by the values passed to this function.
	 * 
	 * @param name
	 * @param type
	 * @param world
	 * @param owner
	 * @return
	 */
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
	
	/**
	 * Checks if a place exists by the Place passed to this function.
	 * 
	 * @param name
	 * @param type
	 * @param world
	 * @param owner
	 * @return
	 */
	public static boolean placeExists(Place place) {
		String query = "SELECT * FROM " + DB_PLACES_TABLENAME +
				" WHERE type='" + place.getType() + "' AND name='" + place.getName() + "' AND owner='" + place.getOwner() + "' AND world='" + place.getWorld() + "'";
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
	 * Takes a Place and inserts into the Places database.
	 * 
	 * @param place
	 * @throws SQLException
	 */
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
	
	/**
	 * Takes a Place and updates its correlating Place database information with values from the object.
	 * 
	 * @param place
	 * @throws SQLException
	 */
	public static void updatePlace(Place place) throws SQLException {
		int discoverable = place.discoverable ? 1 : 0;
		int searchable = place.searchable ? 1 : 0;;
		int pointable = place.pointable ? 1 : 0;;
		
		String update = "UPDATE " + DB_PLACES_TABLENAME + " SET " +
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
				"yaw=" + place.getYaw() + " WHERE " +
				"name='" + place.getName() + "' AND " +
				"type='" + place.getType() + "' AND " +
				"world='" + place.getWorld() + "' AND " +
				"owner='" + place.getOwner() + "'";
		SQLUtil.transactUpdate(update);
	}
	
	/**
	 * Updates the name of a specified Place in the database.
	 * 
	 * @param name
	 * @param type
	 * @param owner
	 * @param world
	 * @param newName
	 * @throws SQLException
	 */
	public static void updateName(String name, String type, String world, String owner, String newName) throws SQLException {
		String update = "UPDATE " + DB_PLACES_TABLENAME + " SET " +
				"name='" + newName + "' WHERE " +
				"name='" + name + "' AND " +
				"type='" + type + "' AND " +
				"world='" + world + "' AND " +
				"owner='" + owner + "'";
		SQLUtil.transactUpdate(update);
	}
	
	/**
	 * Updates the type of a specified Place in the database.
	 * 
	 * @param name
	 * @param type
	 * @param owner
	 * @param world
	 * @param newName
	 * @throws SQLException
	 */
	public static void updateType(String name, String type, String world, String owner, String newType) throws SQLException {
		String update = "UPDATE " + DB_PLACES_TABLENAME + " SET " +
				"type='" + newType + "' WHERE " +
				"name='" + name + "' AND " +
				"type='" + type + "' AND " +
				"world='" + world + "' AND " +
				"owner='" + owner + "'";
		SQLUtil.transactUpdate(update);
	}
	
	/**
	 * Updates the x1, y1, and z1 values of a specified Place in the database.
	 * 
	 * @param name
	 * @param type
	 * @param owner
	 * @param world
	 * @param newName
	 * @throws SQLException
	 */
	public static void updateXYZ1(String name, String type, String world, String owner, double x1, double y1, double z1) throws SQLException {
		String update = "UPDATE " + DB_PLACES_TABLENAME + " SET " +
				"x1=" + x1 + ", " +
				"y1=" + y1 + ", " +
				"z1=" + z1 + " WHERE " +
				"name='" + name + "' AND " +
				"type='" + type + "' AND " +
				"world='" + world + "' AND " +
				"owner='" + owner + "'";
		SQLUtil.transactUpdate(update);
	}
	
	/**
	 * Updates the x2, y2, and z2 values of a specified Place in the database.
	 * 
	 * @param name
	 * @param type
	 * @param owner
	 * @param world
	 * @param newName
	 * @throws SQLException
	 */
	public static void updateXYZ2(String name, String type, String world, String owner, double x2, double y2, double z2) throws SQLException {
		String update = "UPDATE " + DB_PLACES_TABLENAME + " SET " +
				"x1=" + x2 + ", " +
				"y1=" + y2 + ", " +
				"z1=" + z2 + " WHERE " +
				"name='" + name + "' AND " +
				"type='" + type + "' AND " +
				"world='" + world + "' AND " +
				"owner='" + owner + "'";
		SQLUtil.transactUpdate(update);
	}
	
	/**
	 * Updates the yaw value of a specified Place in the database.
	 * 
	 * @param name
	 * @param type
	 * @param owner
	 * @param world
	 * @param newName
	 * @throws SQLException
	 */
	public static void updateYaw(String name, String type, String world, String owner, double yaw) throws SQLException {
		String update = "UPDATE " + DB_PLACES_TABLENAME + " SET " +
				"yaw=" + yaw + " " + " WHERE " +
				"name='" + name + "' AND " +
				"type='" + type + "' AND " +
				"world='" + world + "' AND " +
				"owner='" + owner + "'";
		SQLUtil.transactUpdate(update);
	}
	
	public static void teleportPlayerToPlace(Player subject, Place place) {
		Location loc = place.generateLocation();
		subject.teleport(loc);
	}

}
