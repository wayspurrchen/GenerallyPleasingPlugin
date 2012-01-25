package me.theheyway.GPP.Util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
		
		//if (Constants.VERBOSE) GPP.consoleInfo("Connected to db");
		
		return conne;
	}
	
	public static Connection getConnection() throws SQLException {
		Connection conne = null;
		
		Properties connectionProps = new Properties();
		connectionProps.put("user", Constants.MYSQL_USERNAME);
		connectionProps.put("password", Constants.MYSQL_PASSWORD);
		
		conne = DriverManager.getConnection("jdbc:mysql://" + Constants.MYSQL_HOSTNAME +
				":" + Constants.MYSQL_PORT + "/" + Constants.MYSQL_DBNAME, connectionProps);
		
		//if (Constants.VERBOSE) GPP.consoleInfo("Connected to db");
		
		return conne;
	}
	
	public static boolean databaseExists(String databaseName) throws SQLException {
		Connection conn = SQLUtil.getDefaultConnection();
		
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
	
	public static void createDatabase(String databaseName) throws SQLException {
		Connection conn = null;
		try {
			conn = SQLUtil.getDefaultConnection();
			String transaction = "CREATE DATABASE " + databaseName + ";";
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(transaction);
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			GPP.logger.severe("Rolled back transaction.");
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
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
	
	public static void dropTable(String tableName) throws SQLException {
		
		Connection conn = null;
		try {
			conn = SQLUtil.getConnection();
			String transaction = "DROP TABLE " + tableName;
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(transaction);
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			GPP.logger.severe("Rolled back transaction.");
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
	}
	
	public static void transactUpdate(String execute) throws SQLException {
		Connection conn = null;
		try {
			conn = SQLUtil.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(execute);
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			GPP.logger.severe("Rolled back transaction.");
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
	}
	
	public static boolean anyQualifiedValueExists(String table, String column, String qualifier) throws SQLException {
		return specificQualifiedValueExists(table, "*", column, qualifier);
	}
	
	public static boolean specificQualifiedValueExists(String table, String value, String column, String qualifier) throws SQLException {
		String newQualifier = "";
		
		//Puts the qualifier in quotes if it's not a number
		if (TypeUtil.isDouble(qualifier)) newQualifier = qualifier;
		else newQualifier = "'" + qualifier + "'";
		
		String query = "SELECT " + value + " FROM " + table +
				" WHERE " + column + "=" + newQualifier;
		return queryExistence(query);
	}
	
	private static boolean queryExistence(String queryString) throws SQLException {
		String query = queryString;
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
			GPP.logger.severe("Rolled back transaction.");
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
	}
	
	public static ArrayList<String> getIntValues(String value, String table, String column, String qualifier, int columnRequest) throws SQLException {
		String query = "SELECT " + value + " FROM " + table +
				" WHERE " + column + "='" + qualifier + "'";
		Connection conn = null;
		ArrayList<String> values = new ArrayList<String>();
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				//GPP.consoleInfo("getIntValues it: " + rs.getInt(columnRequest));
				values.add(String.valueOf(rs.getInt(columnRequest)));
			}
			conn.commit();
			conn.close();
			return values;
		} catch (SQLException e) {
			GPP.logger.severe("Rolled back transaction.");
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
	}
	
	public static ArrayList<String> getStringValues(String value, String table, String column, String qualifier, int columnRequest) throws SQLException {
		String query = "SELECT " + value + " FROM " + table +
				" WHERE " + column + "='" + qualifier + "'";
		Connection conn = null;
		ArrayList<String> values = new ArrayList<String>();
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				//GPP.consoleInfo("getStringValues it: " + rs.getString(columnRequest));
				values.add(String.valueOf(rs.getString(columnRequest)));
			}
			conn.commit();
			conn.close();
			return values;
		} catch (SQLException e) {
			GPP.logger.severe("Rolled back transaction.");
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
	}
	
	public static ArrayList<String> getDoubleValues(String value, String table, String column, String qualifier, int columnRequest) throws SQLException {
		String query = "SELECT " + value + " FROM " + table +
				" WHERE " + column + "='" + qualifier + "'";
		Connection conn = null;
		ArrayList<String> values = new ArrayList<String>();
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				//GPP.consoleInfo("getStringValues it: " + rs.getDouble(columnRequest));
				values.add(String.valueOf(rs.getDouble(columnRequest)));
			}
			conn.commit();
			conn.close();
			return values;
		} catch (SQLException e) {
			GPP.logger.severe("Rolled back transaction.");
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
	}
	
	//This series of get* methods assumes that the ResultSet returned is only one row and one column in size, in most cases.
	//That's how I'm mostly using them, at least.
	
	//However, the single-parameter version of these functions will accept any query, allowing for more complex queries.
	
	public static String getInt(String value, String table, String column, String qualifier) throws SQLException {
		//TODO: Add conditionals for this and similar functions for checking double/integer/string values passed
		//so we can check whether or not the query should have quotation marks for some values.
		String query = "SELECT " + value + " FROM " + table +
				" WHERE " + column + "='" + qualifier + "'";
		return getInt(query);
	}
	
	public static String getString(String value, String table, String column, String qualifier) throws SQLException {
		//TODO: Add conditionals for this and similar functions for checking double/integer/string values passed
		//so we can check whether or not the query should have quotation marks for some values.
		String query = "SELECT " + value + " FROM " + table +
				" WHERE " + column + "='" + qualifier + "'";
		return getString(query);
	}
	
	public static String getDouble(String value, String table, String column, String qualifier) throws SQLException {
		//TODO: Add conditionals for this and similar functions for checking double/integer/string values passed
		//so we can check whether or not the query should have quotation marks for some values.
		String query = "SELECT " + value + " FROM " + table +
				" WHERE " + column + "='" + qualifier + "'";
		return getDouble(query);
	}
	
	public static String getInt(String SQLquery) throws SQLException {
		String query = SQLquery;
		Connection conn = null;
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			if (!rs.next()) return null;
			String accountno = String.valueOf(rs.getInt(1));
			conn.commit();
			conn.close();
			return accountno;
		} catch (SQLException e) {
			GPP.logger.severe("Rolled back transaction.");
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
	}
	
	public static String getString(String SQLquery) throws SQLException {
		String query = SQLquery;
		Connection conn = null;
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			if (!rs.next()) return null;
			String accountno = String.valueOf(rs.getString(1));
			conn.commit();
			conn.close();
			return accountno;
		} catch (SQLException e) {
			GPP.logger.severe("Rolled back transaction.");
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
	}
	
	public static String getDouble(String SQLquery) throws SQLException {
		String query = SQLquery;
		Connection conn = null;
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			if (!rs.next()) return null;
			String accountno = String.valueOf(rs.getDouble(1));
			conn.commit();
			conn.close();
			return accountno;
		} catch (SQLException e) {
			GPP.logger.severe("Rolled back transaction.");
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
	}
	
	public static double[] getTopDoubles(String value, String table, String orderby, int count) throws SQLException {
		String query = "SELECT " + value + " FROM " + table
				+ " ORDER BY " + orderby + " DESC";
		double[] tops = new double[count];
		Connection conn = null;
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			if (!rs.next()) return null;
			for (int i = 0; i < count; i++) {
				if (!rs.isAfterLast()) tops[i] = rs.getDouble(1);
				else break;
			}
			conn.commit();
			conn.close();
			return tops;
		} catch (SQLException e) {
			GPP.logger.severe("Rolled back transaction.");
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
	}
	
	public static String[] getTopStrings(String value, String table, String orderby, int count) throws SQLException {
		String query = "SELECT " + value + " FROM " + table
				+ " ORDER BY " + orderby + " DESC";
		String[] tops = new String[count];
		Connection conn = null;
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			if (!rs.next()) return null;
			for (int i = 0; i < count; i++) {
				if (!rs.isAfterLast()) tops[i] = rs.getString(1);
				else break;
			}
			conn.commit();
			conn.close();
			return tops;
		} catch (SQLException e) {
			GPP.logger.severe("Rolled back transaction.");
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
	}
	
}
