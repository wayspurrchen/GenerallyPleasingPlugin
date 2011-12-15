package me.theheyway.GPP.Economos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import me.theheyway.GPP.Constants;
import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Economos.CashIn.CashIn;
import me.theheyway.GPP.Economos.EconomosConstants;
import me.theheyway.GPP.Economos.Shops.Shops;
import me.theheyway.GPP.Util.SQLUtil;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Economos {
	
	private GPP plugin;
	
	public EconomosCommands commands;
	public AccountManager accMan;
	public EconomosConstants constants;
	
	static File economos_config_file = new File(EconomosConstants.ECONOMOS_CONFIG_PATH);
	static FileConfiguration economos_config = null;
	
	public CashIn cashin;
	public Shops shops;
	
	public Economos(GPP plugin) {
		this.plugin = plugin;
		
		//Set up module config
		reloadConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();

		constants = new EconomosConstants(plugin, this);
		commands = new EconomosCommands(plugin, this);
		
		//Check if modules are enabled
		if (getConfig().getBoolean("Modules.CashIn.Enabled", false)) {
			GPP.consoleInfo("[Economos] CashIn enabled.");
			cashin = new CashIn(plugin, this);
		} else GPP.consoleInfo("[Economos] CashIn disabled.");
		
		if (getConfig().getBoolean("Modules.Shops.Enabled", true)) {
			GPP.consoleInfo("[Economos] Shops enabled.");
			shops = new Shops(this);
			//pm.registerEvent(Event.Type.PLAYER_PICKUP_ITEM, cashInPlayerListener, Event.Priority.Normal, this);
		} else GPP.consoleInfo("[Economos] Shops disabled.");
		
		try {
			dbConstruction();
			SQLUtil.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Core Economos commands
		plugin.getCommand("ec").setExecutor(this.commands);
		plugin.getCommand("account").setExecutor(this.commands);
		plugin.getCommand("balance").setExecutor(this.commands);
		plugin.getCommand("setbalance").setExecutor(this.commands);
		plugin.getCommand("pay").setExecutor(this.commands);

		//Economos CashIn commands
		plugin.getCommand("level").setExecutor(this.cashin.commands);
		plugin.getCommand("setlevel").setExecutor(this.cashin.commands);
		plugin.getCommand("cashin").setExecutor(this.cashin.commands);
		//plugin.getCommand("exchangerate").setExecutor(this.cashin.commands);
		
	}
	
	private void dbConstruction() throws SQLException {
		if (!SQLUtil.databaseExists(Constants.MYSQL_DBNAME)) {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[Economos] Database not found. Creating...");
			if (SQLUtil.createDatabase(Constants.MYSQL_DBNAME)) {
				if (EconomosConstants.VERBOSE) GPP.consoleInfo("[Economos] Database " + Constants.MYSQL_DBNAME + " created. Yippee-kay-yay!");
			}
		} else if (EconomosConstants.VERBOSE) GPP.consoleInfo("[Economos] Database found.");
		
		if (!SQLUtil.tableExists(EconomosConstants.DB_WALLET_TABLENAME)) {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[Economos] Individual account table not found. Creating.");
			Connection conn = SQLUtil.getConnection();
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);
			String execute = "CREATE TABLE " + EconomosConstants.DB_WALLET_TABLENAME
					+ " (id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
					"user CHAR(128) UNIQUE, " +
					"balance DECIMAL)";
			stmt.executeUpdate(execute);
			conn.commit();
			conn.close();
		} else GPP.consoleInfo("[Economos] Individual account table found.");
		if (!SQLUtil.tableExists(EconomosConstants.DB_ACCOUNTS_TABLENAME)) {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[Economos] Accounts table not found. Creating.");
			Connection conn = SQLUtil.getConnection();
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);
			String execute = "CREATE TABLE " + EconomosConstants.DB_ACCOUNTS_TABLENAME
					+ " (id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
					"accountno INTEGER UNIQUE NOT NULL, " +
					"balance DECIMAL, " +
					"interest DECIMAL)";
			stmt.executeUpdate(execute);
			conn.commit();
			conn.close();
		} else GPP.consoleInfo("[Economos] Accounts table found.");
		if (!SQLUtil.tableExists(EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME)) {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[Economos] Account entries table not found. Creating.");
			Connection conn = SQLUtil.getConnection();
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);
			String execute = "CREATE TABLE " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME
					+ " (id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
					"accountno INTEGER NOT NULL, " +
					"user CHAR(128) NOT NULL, " +
					"role CHAR(128) NOT NULL, " +
					"withdrawlimit DECIMAL)";
			stmt.executeUpdate(execute);
			conn.commit();
			conn.close();
		} else GPP.consoleInfo("[Economos] Account entries table found.");
		
	}

	public void reloadConfig() {
		economos_config = YamlConfiguration.loadConfiguration(economos_config_file);
		
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource(EconomosConstants.ECONOMOS_INTERNAL_CONFIG);
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        
	        economos_config.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getConfig() {
		if (economos_config == null) {
			reloadConfig();
		}
		return economos_config;
	}
	
	public void saveConfig() {
		try {
	        economos_config.save(EconomosConstants.ECONOMOS_CONFIG_PATH);
	    } catch (IOException ex) {
	    	GPP.logger.log(Level.SEVERE, "Could not save config to " + economos_config_file, ex);
	    }
	}
		
}