package me.theheyway.GPP.Economos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Util.SQLUtil;

public class Account {
	
	private String activeAccount;
	private String playerName;
	private ArrayList<String> accounts = new ArrayList<String>();
	
	Account(String playerName) {
		this.playerName = playerName;
		if (!hasWallet()) createWallet();
	}
	
	public String playerName() {
		return playerName;
	}
	
	public boolean hasActiveAccount() {
		if (activeAccount!=null) {
			return true;
		} else return false;
	}
	
	public String getActiveAccount() {
		return activeAccount;
	}
	
	public void setActiveAccount(String accountnumber) {
		if (hasAccountNo(accountnumber)) {
			activeAccount = accountnumber;
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[Economos] " + playerName + " set account " + accountnumber + " to active account.");
		} else {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[Economos] " + playerName + " tried to set active account to " + accountnumber + " but account was not found.");
		}
	}
	
	public boolean hasAccount() {
		String accounts = EconomosConstants.DB_ACCOUNTS_TABLENAME;
		String accountEntries = EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME;
		String query = "SELECT " + accountEntries + ".user FROM " + accountEntries +
				" INNER JOIN " + accounts +
				" ON " + accountEntries + ".accountno=" + accounts + ".accountno" +
				" WHERE " + accountEntries + ".user='" + playerName + "'";
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
	
	public boolean hasAccountNo(String accountno) {
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
	
	/**
	 * Checks if player has a wallet account set up in the database.
	 * 
	 * @return true if a player has a wallet account, false if not
	 */
	public boolean hasWallet() {
		if (SQLUtil.valueExists(EconomosConstants.DB_WALLET_TABLENAME, "user", playerName)) return true;
		else return false;
	}
	
	/**
	 * Create a basic wallet account tied to the player.
	 */
	public void createWallet() {
		String execute = "INSERT INTO " + EconomosConstants.DB_WALLET_TABLENAME + " (user, balance)" +
				" VALUES ('" + playerName + "', " + EconomosConstants.WALLET_INITIAL_BALANCE + ")";
		SQLUtil.transactUpdate(execute);
		if (EconomosConstants.VERBOSE) GPP.consoleInfo("[Economos] Individual wallet account created for " + playerName + ".");
	}
	
	/**
	 * Creates an account entry with the player's name and account number. Balance initialized at 0.
	 * 
	 * @param accountnumber - account number to be inputted
	 */
	public void createAccount(String accountnumber) {
		String execute = "INSERT INTO " + EconomosConstants.DB_ACCOUNTS_TABLENAME + " (accountno, balance, interest)" +
				" VALUES (" + accountnumber + ", 0.0, " + EconomosConstants.INTEREST_AMOUNT + ")";
		SQLUtil.transactUpdate(execute);
		if (EconomosConstants.VERBOSE) GPP.consoleInfo("[Economos] Bank account " + accountnumber + " created for " + playerName + ".");
	}
	
	public String getAnyOwnedAccount() {
		return SQLUtil.getFirstValue("accountno", EconomosConstants.DB_ACCOUNTS_TABLENAME, "user", playerName);
	}
	
	public double getWalletBalance() {
		return Double.parseDouble(SQLUtil.getFirstValue("balance", EconomosConstants.DB_WALLET_TABLENAME, "user", playerName));
	}
	
	public void setWalletBalance(Double value) {
		String execute = "UPDATE " + EconomosConstants.DB_WALLET_TABLENAME +
				" SET balance=" + value + " WHERE user='" + playerName + "'";
		SQLUtil.transactUpdate(execute);
	}
	
	public void incrementWalletBalance(Double value) {
		value += getWalletBalance();
		String execute = "UPDATE " + EconomosConstants.DB_WALLET_TABLENAME +
				" SET balance=" + value + " WHERE user='" + playerName + "'";
		SQLUtil.transactUpdate(execute);
	}

}
