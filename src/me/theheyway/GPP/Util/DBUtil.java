package me.theheyway.GPP.Util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.theheyway.GPP.Constants;
import me.theheyway.GPP.GPP;

public class DBUtil {
	
	public static Connection conn = null;
	
	public static Connection getConnection() throws SQLException {
		//Connection conn = null;
		conn = DriverManager.getConnection("jdbc:sqlite:" + Constants.SQLITE_DB_PATH);
		
		return conn;
	}
	
	public static boolean tableExists(String string) throws SQLException {
		//Connection conn = getConnection();
		conn = DBUtil.getConnection();
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet res = meta.getTables(null, null, string, 
				new String[] {"TABLE"});
		conn.close();
		if (res.next()) return true;
		return false;
	}
	
	
	public static Statement beginConnStatement() {
		try {
			conn = getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			return stmt;
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
	
	//this makes no sense but I'm gonna DO IT ANYWAY
	public static void endConn() {
		try {
			conn.close();
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	//I feel so fancy
	public static boolean transactUpdate(String execute) {
		//Connection conn = null;
		try {
			conn = DBUtil.getConnection();
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
	
	public static ResultSet transactQuery(String query) {
		//Connection conn = null;
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
	
	//Assumes only one return value
	public static double grabDouble(String table, String column, String limitcolumn, String limitval) {
		String query = "SELECT " + column + " FROM " + table + " WHERE " + limitcolumn + "='" + limitval + "'";
				
		try {
			conn = getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			rs.next();
			double grabbed = rs.getDouble(column);
			conn.commit();
			conn.close();
			return grabbed;
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return -1.0;
		}
	}
	
	public static boolean update(String table, String column, String newval, String limitcolumn, String limitval) {
		String query = "UPDATE " + table + " SET " + column + "='" + newval + "' WHERE " + limitcolumn + "='" + limitval + "'";
				
		try {
			conn = getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			stat.executeUpdate(query);
			conn.commit();
			conn.close();
			return true;
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
	
}
