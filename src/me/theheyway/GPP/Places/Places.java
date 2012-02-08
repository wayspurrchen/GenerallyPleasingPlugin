package me.theheyway.GPP.Places;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.theheyway.GPP.Constants;
import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Economos.EconomosConstants;
import me.theheyway.GPP.Util.SQLUtil;

public class Places {

	private GPP plugin;
	public PlacesCommands commands;
	
	public static final String DB_LOCATIONS_TABLENAME = "Locations";
	public static final String DB_PLACES_TABLENAME = "Places";
	
	public static final String SPAWNS_FILE_PATH = "plugins/GPP/spawns.data";
	
	public static File SPAWNS_FILE = new File(SPAWNS_FILE_PATH);
	public static FileConfiguration SPAWNS_FILECONFIG = null;

	public Places(GPP plugin) {
		this.plugin = plugin;
		this.commands = new PlacesCommands(plugin);
		
		try {
			placesDatabaseConstruction();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void placesDatabaseConstruction() throws SQLException {
		if (!SQLUtil.databaseExists(Constants.MYSQL_DBNAME)) {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP] Database not found. Creating...");
			SQLUtil.createDatabase(Constants.MYSQL_DBNAME);
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP] Database " + Constants.MYSQL_DBNAME + " created. Yippee-kay-yay!");
		} else if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP] Database found.");
		
		//Create Places DB if it doesn't exist
		if (!SQLUtil.tableExists(DB_PLACES_TABLENAME)) {
			GPP.logger.info("[GPP] Places MySQL table not found; creating....");
			Connection conn = SQLUtil.getConnection();
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);
			String execute = "CREATE TABLE " + DB_PLACES_TABLENAME +
					" (id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
					"name CHAR(128) NOT NULL, " +
					"description VARCHAR(512), " +
					"type CHAR(128) NOT NULL, " +
					"world CHAR(128) NOT NULL," +
					"owner CHAR(128) NOT NULL, " +
					"shape CHAR(128) NOT NULL, " +
					"discoverable BIT(1), " +
					"expreward INT, " +
					"searchable BIT(1), " +
					"pointable BIT(1), " +
					"x1 DOUBLE NOT NULL," +
					"y1 DOUBLE NOT NULL," +
					"z1 DOUBLE NOT NULL," +
					"x2 DOUBLE," +
					"y2 DOUBLE," +
					"z2 DOUBLE," +
					"yaw DOUBLE)";
			stmt.executeUpdate(execute);
			conn.commit();
			conn.close();
			GPP.logger.info("[GPP] Places MySQL table created.");
		} else GPP.logger.info("[GPP] Places table found.");
		
		//If the old Locations DB exists, transfer its data to the Places DB and drop the old table.
		//WARNING: This does not check for duplicate entries, so it should never be run twice. Make
		//sure the old table gets dropped.
		if (SQLUtil.tableExists(DB_LOCATIONS_TABLENAME)) {
			GPP.logger.info("[GPP] Leftover Locations database found; attempting to transfer data.");
			Connection conn = SQLUtil.getConnection();
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);
			String execute = "INSERT " + DB_PLACES_TABLENAME +
					" (type, name, owner, world, x1, y1, z1, yaw, shape) SELECT " +
					"type, name, owner, world, x, y, z, yaw, 'point' FROM " + DB_LOCATIONS_TABLENAME;
			stmt.executeUpdate(execute);
			conn.commit();
			conn.close();
			GPP.logger.info("[GPP] Places MySQL table updated.");
			//GPP.logger.info("[GPP] Dropping old Locations table.");
			//SQLUtil.dropTable(DB_LOCATIONS_TABLENAME);
			//GPP.logger.info("[GPP] Locations table dropped.");
		}
	}
	
	public static FileConfiguration getSpawnYaws() {
		if (SPAWNS_FILECONFIG == null) {
			reloadSpawnYaws();
		}
		return SPAWNS_FILECONFIG;
	}
	
	public static void reloadSpawnYaws() {
		SPAWNS_FILECONFIG = YamlConfiguration.loadConfiguration(SPAWNS_FILE);
	}

	public static void saveSpawnYaws() {
		try {
			SPAWNS_FILECONFIG.save(SPAWNS_FILE_PATH);
	    } catch (IOException ex) {
	    	GPP.logger.log(Level.SEVERE, "Could not save config to " + SPAWNS_FILE_PATH, ex);
	    }
	}

}
