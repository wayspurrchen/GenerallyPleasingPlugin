package me.theheyway.GPP.Util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import me.theheyway.GPP.Constants;
import me.theheyway.GPP.GPP;

public class SQLUtil {
	
	public static Connection getDefaultConnection() throws SQLException {
		Connection conne = null;
		
		Properties connectionProps = new Properties();
		connectionProps.put("user", Constants.MYSQL_USERNAME);
		connectionProps.put("password", Constants.MYSQL_PASSWORD);
		
		conne = DriverManager.getConnection("jdbc:mysql://" + Constants.MYSQL_HOSTNAME +
				":" + Constants.MYSQL_PORT + "/mysql", connectionProps);
		
		if (Constants.VERBOSE) GPP.consoleInfo("Connected to db");
		
		return conne;
	}
	
	public static Connection getConnection() throws SQLException {
		Connection conne = null;
		
		Properties connectionProps = new Properties();
		connectionProps.put("user", Constants.MYSQL_USERNAME);
		connectionProps.put("password", Constants.MYSQL_PASSWORD);
		
		conne = DriverManager.getConnection("jdbc:mysql://" + Constants.MYSQL_HOSTNAME +
				":" + Constants.MYSQL_PORT + "/" + Constants.MYSQL_DBNAME, connectionProps);
		
		if (Constants.VERBOSE) GPP.consoleInfo("Connected to db");
		
		return conne;
	}
	
	public static boolean databaseExists(String databaseName) throws SQLException {
		Connection conn = SQLUtil.getConnection();
		
		String query = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = '" + databaseName + "'";
		Statement stat = conn.createStatement();
		conn.setAutoCommit(false);
		ResultSet rs = stat.executeQuery(query);
		boolean exists = false;
		if (rs.next()) exists = true;
		
		rs.close();
		stat.close();
		conn.close();
		
		return exists;
	}
	
	public static boolean createDatabase(String databaseName) throws SQLException {
		
		Connection conn = null;
		try {
			conn = SQLUtil.getDefaultConnection();
			String transaction = "CREATE DATABASE " + databaseName + ";";
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(transaction);
			conn.commit();
			conn.close();
			return true;
		} catch (SQLException e) {
			try {
				GPP.logger.severe("Rolled back transaction.");
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean tableExists(String string) throws SQLException {
		Connection conn = SQLUtil.getConnection();
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet res = meta.getTables(null, null, string, 
				new String[] {"TABLE"});
		boolean exists = false;
		if (res.next()) exists = true;
		conn.close();
		return exists;
	}
	
	public static boolean transactUpdate(String execute) {
		Connection conn = null;
		try {
			conn = SQLUtil.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(execute);
			conn.commit();
			conn.close();
			return true;
		} catch (SQLException e) {
			try {
				GPP.logger.severe("Rolled back transaction.");
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
	}
	
	//In retrospect this makes no sense, it can't return the ResultSet if the connection is closed already. FARTS
	public static ResultSet transactQuery(String query) {
		Connection conn = null;
		try {
			conn = getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			conn.commit();
			conn.close();
			return rs;
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean valueExists(String table, String column, String qualifier) {
		String query = "SELECT * FROM " + table +
				" WHERE " + column + "='" + qualifier + "'";
		Connection conn = null;
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			boolean exists = false;
			if (rs.next()) exists = true;
			conn.commit();
			conn.close();
			return exists;
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
	}
	
	public static String getFirstValue(String value, String table, String column, String qualifier) {
		String query = "SELECT " + value + " FROM " + table +
				" WHERE " + column + "='" + qualifier + "'";
		Connection conn = null;
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			rs.next();
			String accountno = String.valueOf(rs.getInt(1));
			conn.commit();
			conn.close();
			return accountno;
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return null;
		}
	}
	
	public static String[] getTop(String value, String table, String orderby, int count) {
		String query = "SELECT " + value + " FROM " + table
				+ " ORDER BY " + orderby + " DESC";
		String[] tops = new String[count];
		Connection conn = null;
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			for (int i = 0; i < count; i++) {
				if (rs.next()) tops[i] = String.valueOf(rs.getDouble(i+1));
				else break;
			}
			conn.commit();
			conn.close();
			return tops;
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return null;
		}
	}
	
}
