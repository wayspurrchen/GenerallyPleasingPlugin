package me.theheyway.GPP.Overlord;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import me.theheyway.GPP.Constants;
import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Economos.EconomosConstants;
import me.theheyway.GPP.Util.SQLUtil;

public class Overlord {
	
	private GPP plugin;
	
	//Command Managers
	me.theheyway.GPP.Overlord.General general_commands;
	me.theheyway.GPP.Overlord.Ports ports_commands;
	me.theheyway.GPP.Overlord.Cruelty cruelty_commands;
	me.theheyway.GPP.Overlord.Inquisitor inquisitor_commands;
	
	public Overlord(GPP plugin) {
		this.plugin = plugin;
		
		general_commands = new me.theheyway.GPP.Overlord.General(plugin, this);
		ports_commands = new me.theheyway.GPP.Overlord.Ports(plugin, this);
		cruelty_commands = new me.theheyway.GPP.Overlord.Cruelty(plugin, this);
		inquisitor_commands = new me.theheyway.GPP.Overlord.Inquisitor(plugin, this);
		
		//Core GPP commands
		plugin.getCommand("gpp").setExecutor(general_commands);
		plugin.getCommand("cr").setExecutor(general_commands);
		plugin.getCommand("point").setExecutor(general_commands);
		plugin.getCommand("cm").setExecutor(general_commands);
		plugin.getCommand("test").setExecutor(general_commands);
		plugin.getCommand("flip").setExecutor(general_commands);
		plugin.getCommand("heal").setExecutor(general_commands);
				
		//Ports - spawn, homes, warps, teleport commands
		plugin.getCommand("home").setExecutor(ports_commands);
		plugin.getCommand("jumpto").setExecutor(ports_commands);
		plugin.getCommand("put").setExecutor(ports_commands);
		plugin.getCommand("spawn").setExecutor(ports_commands);
		plugin.getCommand("sethome").setExecutor(ports_commands);
		plugin.getCommand("setspawn").setExecutor(ports_commands);
		plugin.getCommand("summon").setExecutor(ports_commands);
		plugin.getCommand("tp").setExecutor(ports_commands);

		//Cruelty - punishing players for being bad
		plugin.getCommand("freeze").setExecutor(cruelty_commands);
		
		//Overlord - punishing players for being bad
		plugin.getCommand("clear").setExecutor(inquisitor_commands);
		plugin.getCommand("strip").setExecutor(inquisitor_commands);
		
		try {
			dbConstruction();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void dbConstruction() throws SQLException {
		if (!SQLUtil.databaseExists(Constants.MYSQL_DBNAME)) {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP] Database not found. Creating...");
			if (SQLUtil.createDatabase(Constants.MYSQL_DBNAME)) {
				if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP] Database " + Constants.MYSQL_DBNAME + " created. Yippee-kay-yay!");
			}
		} else if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP] Database found.");
		
		if (!SQLUtil.tableExists(Ports.DB_LOCATIONS_TABLENAME)) {
			GPP.logger.info("[GPP] Locations MySQL table not found; creating....");
			Connection conn = SQLUtil.getConnection();
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);
			String execute = "CREATE TABLE " + Ports.DB_LOCATIONS_TABLENAME +
					" (id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
					"type CHAR(128) NOT NULL, " +
					"name CHAR(128) NOT NULL, " +
					"owner CHAR(128) NOT NULL, " +
					"world CHAR(128) NOT NULL," +
					"x DOUBLE NOT NULL," +
					"y DOUBLE NOT NULL," +
					"z DOUBLE NOT NULL," +
					"yaw DOUBLE NOT NULL)";
			stmt.executeUpdate(execute);
			conn.commit();
			conn.close();
			GPP.logger.info("[GPP] Locations SQLite table created.");
		} else GPP.logger.info("[GPP] Locations table found.");
	}

}
