package me.theheyway.GPP.Economos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import me.theheyway.GPP.Constants;
import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Util.SQLUtil;

public class AccountManager {
	
	private Economos economos;
	private static ArrayList<Account> accounts = new ArrayList<Account>();
	
	AccountManager(Economos economos) {
		this.economos = economos;
	}
	
	public static void createAccountObject(String playerName) {
		accounts.add(new Account(playerName));
	}
	
	public static Account getAccount(String playerName) {
		if (!accounts.isEmpty()) {
			for (int i = 0; i < accounts.size(); i++) {
				GPP.consoleInfo("looping through accounts:" +  accounts.get(i).playerName());
				if (accounts.get(i).playerName().equals(playerName)) {
					if(Constants.VERBOSE) GPP.consoleInfo("AccountManager.getAccount found" +  accounts.get(i).playerName() + " to match " + playerName);
					return accounts.get(i);
				}
			}
		} else return null;
		if(Constants.VERBOSE) GPP.consoleInfo("[Economos] Tried to getAccount for player " + playerName + " but no account was found.");
		return null;
	}
	
	public static boolean hasAccountObject(String playerName) {
		if (getAccount(playerName)!=null) {
			return true;
		}
		return false;
	}
	
	public boolean hasAccountNo(String playerName, String accountno) {
		String query = "SELECT " + accountno + " FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME +
				" WHERE user='" + playerName + "'";
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
	
	public String getAnyAccount(String playerName) {
		String accountEntries = EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME;
		String query = "SELECT accountno FROM " + accountEntries +
				" WHERE user='" + playerName + "'";
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
	
	public double getWalletBalance(String playerName) {
		return Double.parseDouble(SQLUtil.getFirstValue("balance", EconomosConstants.DB_WALLET_TABLENAME, "user", playerName));
	}
	
	public double getAccountBalance(String accountno) {
		return Double.parseDouble(SQLUtil.getFirstValue("balance", EconomosConstants.DB_ACCOUNTS_TABLENAME, "accountno", accountno));
	}
	
	public void setIndividualBalance(String playerName, Double value) {
		String execute = "UPDATE " + EconomosConstants.DB_WALLET_TABLENAME +
				" SET balance=" + value + " WHERE user='" + playerName + "'";
		SQLUtil.transactUpdate(execute);
	}
	
	public void incrementIndividualBalance(String playerName, Double value) {
		value += getWalletBalance(playerName);
		String execute = "UPDATE " + EconomosConstants.DB_WALLET_TABLENAME +
				" SET balance=" + value + " WHERE user='" + playerName + "'";
		SQLUtil.transactUpdate(execute);
	}
	
}
