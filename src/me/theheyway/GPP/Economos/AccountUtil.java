package me.theheyway.GPP.Economos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.theheyway.GPP.Constants;
import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Util.SQLUtil;

/**
 * Utility class; all queries that do not deal with accountaints explicitly query/transact the MySQL Database for information.
 * 
 * @author Way
 *
 */
public class AccountUtil {
	
	private Economos economos;
	private static ArrayList<Accountant> accounts = new ArrayList<Accountant>();
	
	AccountUtil(Economos economos) {
		this.economos = economos;
	}
	
	
	
	public static Accountant createAccountant(String playerName) {
		Accountant acc = new Accountant(playerName);
		accounts.add(acc);
		return acc;
	}
	
	public static boolean hasAccountant(String playerName) {
		if (getAccountant(playerName)!=null) {
			return true;
		}
		return false;
	}
	
	public static Accountant guaranteeAccountant(String playerName) {
		if (hasAccountant(playerName)) {
			Accountant acc = getAccountant(playerName);
			return acc;
		} else {
			Accountant acc = createAccountant(playerName);
			if (acc != null) {
				return acc;
			} else {
				GPP.consoleSevere("[GPP::ECONOMOS] Could not create account for " + playerName + " with guaranteeAccountObject!");
				//This should never happen
				return null;
			}
		}
	}
	
	public static Accountant getAccountant(Player player) {
		String playerName = player.getName();
		return getAccountant(playerName);
	}
	
	public static Accountant getAccountant(String playerName) {
		if (!accounts.isEmpty()) {
			for (int i = 0; i < accounts.size(); i++) {
				//GPP.consoleInfo("looping through accounts:" +  accounts.get(i).playerName());
				if (accounts.get(i).playerName().equals(playerName)) {
					//if(Constants.VERBOSE) GPP.consoleInfo("AccountManager.getAccount found" +  accounts.get(i).playerName() + " to match " + playerName);
					return accounts.get(i);
				}
			}
		} else return null;
		if(Constants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Tried to getAccount for player " + playerName + " but no account was found.");
		return null;
	}
	
	/**
	 * Tries to grab an account from a player's Accountant object. Returns null if no such account exists.
	 * 
	 * @param playerName
	 * @param accountNumber
	 * @return
	 */
	public static Account grabAccount(String playerName, String accountno) {
		return getAccountant(playerName).getAccountByNumber(accountno);
	}
	
	public static boolean accountNoExists(String accountno) {
		return SQLUtil.anyQualifiedValueExists(EconomosConstants.DB_ACCOUNTS_TABLENAME, "accountno", accountno);
	}
	
	public static boolean hasAccountNo(String playerName, String accountno) {
		return SQLUtil.specificQualifiedValueExists(EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME, accountno, "user", playerName);
	}
	
	/**
	 * Creates an account by inserting the proper account value into the Accounts DB table and creating a corresponding
	 * account ownership entry in the AccountEntries DB table.
	 * 
	 * @param playerName - Account player's name
	 * @param accountnumber - Number of account to be made
	 * @param interest - Interest for account
	 * @param withdrawlimit - Withdraw limit for player. Set to 0.0 for owners
	 */
	public static boolean createAccountAndAccountEntry(String playerName, String accountnumber, double interest, double withdrawlimit) {
		if (!accountNoExists(accountnumber)) {
			//Insert Account into Accounts Table
			String execute = "INSERT INTO " + EconomosConstants.DB_ACCOUNTS_TABLENAME + " (accountno, balance, interest)" +
					" VALUES (" + accountnumber + ", " + interest + ", " + EconomosConstants.INTEREST_AMOUNT + ")";
			if (!SQLUtil.transactUpdate(execute)) {
				if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Account " + accountnumber + " Account MySQL DB INSERT transaction failed.");
				return false;
			}
			
			//Insert Account Entry into AccountEntries
			execute = "INSERT INTO " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME + " (accountno, user, role, withdrawlimit)" +
					" VALUES (" + accountnumber + ", '" + playerName + "', 'owner', " + withdrawlimit + ")";
			if (!SQLUtil.transactUpdate(execute)) {
				if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Account " + accountnumber + " AccountEntry MySQL DB INSERT transaction failed.");
				return false;
			}
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Account " + accountnumber + " created for " + playerName + ".");
			return true;
		} else {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Tried to create account " + accountnumber + " but it already exists!");
			return false;
		}
	}
	
	/**
	 * Creates an account without an attached player/owner
	 * 
	 * @param playerName
	 * @param accountnumber
	 * @param interest
	 * @param withdrawlimit
	 */
	public static void createAccount(String accountnumber, double interest) {
		//Insert Account into Accounts Table
		String execute = "INSERT INTO " + EconomosConstants.DB_ACCOUNTS_TABLENAME + " (accountno, balance, interest)" +
				" VALUES (" + accountnumber + ", " + interest + ", " + EconomosConstants.INTEREST_AMOUNT + ")";
		SQLUtil.transactUpdate(execute);
		if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Bank account " + accountnumber + " created.");
	}
	
	public static boolean createAccountEntry(String playerName, String accountno, double withdrawlimit, String role) {
		//TODO: Insert check for duplicates!
		//Insert Account Entry into AccountEntries
		String execute = "INSERT INTO " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME + " (accountno, user, role, withdrawlimit)" +
				" VALUES (" + accountno + ", '" + playerName + "', '" + role + "', " + withdrawlimit + ")";
		if (SQLUtil.transactUpdate(execute)) {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] AccountEntry created for account " + accountno + " for " + playerName + ".");
			return true;
		} else {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] AccountEntry creation MySQL DB transaction for account " + accountno + " for " + playerName + " failed. Check logs for more information.");
			return false;
		}
		
	}
	
	public static boolean deleteAccountEntry(String playerName, String accountno) {
		//Insert Account Entry into AccountEntries
		String execute = "DELETE FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME + " WHERE user='" + playerName + "' AND accountno=" + accountno;
		if (SQLUtil.transactUpdate(execute)) {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] " + playerName + "'s AccountEntry for account " + accountno + " deleted.");
			return true;
		} else {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] AccountEntry deletion transaction of " + playerName + "'s account " + accountno + " failed. Check logs for more information.");
			return false;
		}
	}
	
	public static boolean deleteAccount(String accountno) {
		//Insert Account Entry into AccountEntries
		String execute = "DELETE FROM " + EconomosConstants.DB_ACCOUNTS_TABLENAME + " WHERE accountno=" + accountno;
		if (SQLUtil.transactUpdate(execute)) {
			execute = "DELETE FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME + " WHERE accountno=" + accountno;
			if (SQLUtil.transactUpdate(execute)) {
				if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Account " + accountno + " deleted.");
				return true;
			} else {
				if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Account deletion transaction of account " + accountno + " failed. Check logs for more information.");
				return false;
			}
		} else {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Account deletion transaction of account " + accountno + " failed. Check logs for more information.");
			return false;
		}
	}
	
	public static String getAnyAccount(String playerName) {
		return SQLUtil.getInt("accountno", EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME, "user", playerName);
		/*String accountEntries = EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME;
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
		}*/
	}
	
	/**
	 * Returns the names of the users, regardless of role, of a specified account in an ArrayList<String>. Returns null if empty.
	 * 
	 * @param accountno
	 * @return
	 */
	public static ArrayList<Player> getAccountOnlineUsers(String accountno) {
		ArrayList<String> allUsers = getAccountUsers(accountno);
		ArrayList<Player> onlineUsers = new ArrayList<Player>();
		if (!allUsers.isEmpty()) {
			for (int i=0; i < allUsers.size(); i++) {
				Player player = GPP.server.getPlayerExact(allUsers.get(i));
				if (player!=null) onlineUsers.add(player);
			}
		}
		
		return onlineUsers;
	}
	
	/**
	 * Returns the names of the users, regardless of role, of a specified account in an ArrayList<String>.
	 * 
	 * @param accountno
	 * @return
	 */
	public static ArrayList<String> getAccountUsers(String accountno) {
		String query = "SELECT user FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME +
				" WHERE accountno=" + accountno;
		Connection conn = null;
		ArrayList<String> users = new ArrayList<String>();
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				users.add(String.valueOf(rs.getString(1)));
			}
			conn.commit();
			conn.close();
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
		return users;
	}
	
	/**
	 * Returns the names of the owners of a specified account in an ArrayList<String>.
	 * 
	 * @param accountno
	 * @return
	 */
	public static ArrayList<String> getAccountOwners(String accountno) {
		String query = "SELECT user FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME +
				" WHERE accountno=" + accountno + " AND role='owner'";
		Connection conn = null;
		ArrayList<String> users = new ArrayList<String>();
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				users.add(String.valueOf(rs.getString(1)));
			}
			conn.commit();
			conn.close();
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
		return users;
	}
	
	public static boolean isOwner(String playerName, String accountno) {
		String query = "SELECT role FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME +
				" WHERE accountno=" + accountno + " AND user='" + playerName + "'";
		Connection conn = null;
		boolean isOwner = false;
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			if (rs.next()) {
				if (rs.getString(1).equals("owner")) {
					isOwner = true;
				}
			} else return false;
			conn.commit();
			conn.close();
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
		return isOwner;
	}
	
	public static boolean isUser(String playerName, String accountno) {
		String query = "SELECT role FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME +
				" WHERE accountno=" + accountno + " AND user='" + playerName + "'";
		Connection conn = null;
		boolean isUser = false;
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			if (rs.next()) {
				String role = rs.getString(1);
				if (role.equals("user") || role.equals("owner")) {
					isUser = true;
				}
			} else return false;
			conn.commit();
			conn.close();
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
		return isUser;
	}
	
	protected static double getWalletBalance(String playerName) {
		return Double.parseDouble(SQLUtil.getInt("balance", EconomosConstants.DB_WALLET_TABLENAME, "user", playerName));
	}
	
	protected static double getAccountBalance(String accountno) {
		return Double.parseDouble(SQLUtil.getInt("balance", EconomosConstants.DB_ACCOUNTS_TABLENAME, "accountno", accountno));
	}
	
	protected static double getAccountInterest(String accountno) {
		return Double.parseDouble(SQLUtil.getInt("interest", EconomosConstants.DB_ACCOUNTS_TABLENAME, "accountno", accountno));
	}
	
	protected static String getAccountUserRole(String accountno, String playerName) {
		return SQLUtil.getString("SELECT role FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME +
				" WHERE accountno=" + accountno + " AND user='" + playerName + "'");
	}
	
	protected static double getAccountWithdrawLimit(String accountno, String playerName) {
		return Double.parseDouble(SQLUtil.getString("SELECT withdrawlimit FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME +
				" WHERE accountno=" + accountno + " AND user='" + playerName + "'"));
	}
	
	public static void setAccountBalance(String accountNo, Double value) {
		String execute = "UPDATE " + EconomosConstants.DB_ACCOUNTS_TABLENAME +
				" SET balance=" + value + " WHERE accountno=" + accountNo + "";
		SQLUtil.transactUpdate(execute);
	}
	
	public static void incrementAccountBalance(String accountNo, Double value) {
		value += getAccountBalance(accountNo);
		String execute = "UPDATE " + EconomosConstants.DB_ACCOUNTS_TABLENAME +
				" SET balance=" + value + " WHERE accountno=" + accountNo + "";
		SQLUtil.transactUpdate(execute);
	}
	
	public static void decrementAccountBalance(String accountNo, Double value) {
		value = getAccountBalance(accountNo) - value;
		String execute = "UPDATE " + EconomosConstants.DB_ACCOUNTS_TABLENAME +
				" SET balance=" + value + " WHERE accountno=" + accountNo + "";
		SQLUtil.transactUpdate(execute);
	}
	
	public static void setWalletBalance(String playerName, Double value) {
		String execute = "UPDATE " + EconomosConstants.DB_WALLET_TABLENAME +
				" SET balance=" + value + " WHERE user='" + playerName + "'";
		SQLUtil.transactUpdate(execute);
	}
	
	public static void incrementWalletBalance(String playerName, Double value) {
		value += getWalletBalance(playerName);
		String execute = "UPDATE " + EconomosConstants.DB_WALLET_TABLENAME +
				" SET balance=" + value + " WHERE user='" + playerName + "'";
		SQLUtil.transactUpdate(execute);
	}
	
	public static void decrementWalletBalance(String playerName, Double value) {
		value = getWalletBalance(playerName) - value;
		String execute = "UPDATE " + EconomosConstants.DB_WALLET_TABLENAME +
				" SET balance=" + value + " WHERE user='" + playerName + "'";
		SQLUtil.transactUpdate(execute);
	}
	
}
