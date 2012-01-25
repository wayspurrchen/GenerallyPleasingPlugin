package me.theheyway.GPP.Economos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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
	
	
	
	public static Accountant createAccountant(String playerName) throws SQLException {
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
	
	public static Accountant guaranteeAccountant(String playerName) throws SQLException {
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
	
	public static boolean walletExists(String playerName) throws SQLException {
		return SQLUtil.anyQualifiedValueExists(EconomosConstants.DB_WALLET_TABLENAME, "user", playerName);
	}
	
	public static boolean accountNoExists(String accountno) throws SQLException {
		return SQLUtil.anyQualifiedValueExists(EconomosConstants.DB_ACCOUNTS_TABLENAME, "accountno", accountno);
	}
	
	public static boolean hasAccountNo(String playerName, String accountno) throws SQLException {
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
	 * @throws SQLException 
	 */
	public static void createAccountAndAccountEntry(String playerName, String accountnumber, double interest, double withdrawlimit) throws SQLException {
		if (!accountNoExists(accountnumber)) {
			//Insert Account into Accounts Table
			String execute = "INSERT INTO " + EconomosConstants.DB_ACCOUNTS_TABLENAME + " (accountno, balance, interest)" +
					" VALUES (" + accountnumber + ", " + interest + ", " + EconomosConstants.INTEREST_AMOUNT + ")";
			SQLUtil.transactUpdate(execute);
			//if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Account " + accountnumber + " Account MySQL DB INSERT transaction failed.");
			
			
			//Insert Account Entry into AccountEntries
			execute = "INSERT INTO " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME + " (accountno, user, role, withdrawlimit)" +
					" VALUES (" + accountnumber + ", '" + playerName + "', 'owner', " + withdrawlimit + ")";
			SQLUtil.transactUpdate(execute);
			//if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Account " + accountnumber + " AccountEntry MySQL DB INSERT transaction failed.");
			
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Account " + accountnumber + " created for " + playerName + ".");
			
		} else {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Tried to create account " + accountnumber + " but it already exists!");
		}
	}
	
	/**
	 * Creates an account without an attached player/owner
	 * 
	 * @param playerName
	 * @param accountnumber
	 * @param interest
	 * @param withdrawlimit
	 * @return 
	 * @throws SQLException 
	 */
	public static void createAccount(String accountnumber, double interest) throws SQLException {
		//Insert Account into Accounts Table
		String execute = "INSERT INTO " + EconomosConstants.DB_ACCOUNTS_TABLENAME + " (accountno, balance, interest)" +
				" VALUES (" + accountnumber + ", " + interest + ", " + EconomosConstants.INTEREST_AMOUNT + ")";
		SQLUtil.transactUpdate(execute);
		if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Bank account " + accountnumber + " created.");
		
	}
	
	public static void createAccountEntry(String playerName, String accountno, double withdrawlimit, String role) throws SQLException {
		//TODO: Insert check for duplicates!
		//Insert Account Entry into AccountEntries
		String execute = "INSERT INTO " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME + " (accountno, user, role, withdrawlimit)" +
				" VALUES (" + accountno + ", '" + playerName + "', '" + role + "', " + withdrawlimit + ")";
		SQLUtil.transactUpdate(execute);
		if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] AccountEntry created for account " + accountno + " for " + playerName + ".");
		
		//if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] AccountEntry creation MySQL DB transaction for account " + accountno + " for " + playerName + " failed. Check logs for more information.");
		
	}
	
	public static void deleteAccountEntry(String playerName, String accountno) throws SQLException {
		//Insert Account Entry into AccountEntries
		String execute = "DELETE FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME + " WHERE user='" + playerName + "' AND accountno=" + accountno;
		SQLUtil.transactUpdate(execute);
		if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] " + playerName + "'s AccountEntry for account " + accountno + " deleted.");
		
		//if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] AccountEntry deletion transaction of " + playerName + "'s account " + accountno + " failed. Check logs for more information.");

	}
	
	public static void deleteAccount(String accountno) throws SQLException {
		//Insert Account Entry into AccountEntries
		String execute = "DELETE FROM " + EconomosConstants.DB_ACCOUNTS_TABLENAME + " WHERE accountno=" + accountno;
		SQLUtil.transactUpdate(execute);
		
		execute = "DELETE FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME + " WHERE accountno=" + accountno;
		SQLUtil.transactUpdate(execute);
		if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Account " + accountno + " deleted.");
		
		//if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Account deletion transaction of account " + accountno + " failed. Check logs for more information.");
		
		//if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] Account deletion transaction of account " + accountno + " failed. Check logs for more information.");

	}
	
	public static String getAnyAccount(String playerName) throws SQLException {
		return SQLUtil.getInt("accountno", EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME, "user", playerName);
		
	}
	
	/**
	 * Returns the names of the users, regardless of role, of a specified account in an ArrayList<String>. Returns null if empty.
	 * 
	 * @param accountno
	 * @return
	 * @throws SQLException 
	 */
	public static ArrayList<Player> getAccountOnlineUsers(String accountno) throws SQLException {
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
	 * @throws SQLException 
	 */
	public static ArrayList<String> getAccountUsers(String accountno) throws SQLException {
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
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
		return users;
	}
	
	/**
	 * Returns the names of the owners of a specified account in an ArrayList<String>.
	 * 
	 * @param accountno
	 * @return
	 * @throws SQLException 
	 */
	public static ArrayList<String> getAccountOwners(String accountno) throws SQLException {
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
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
		return users;
	}
	
	public static boolean isOwner(String playerName, String accountno) throws SQLException {
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
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
		return isOwner;
	}
	
	public static boolean isUser(String playerName, String accountno) throws SQLException {
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
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
		return isUser;
	}
	
	/**
	 * Heavy function that returns all information regarding a specific player's accounts. Account number, balance, interest, user,
	 * role, and withdraw limit. Returns an ArrayList of ArrayList<String>s.
	 * 
	 * Index setup:
	 * 0 = accountno
	 * 1 = balance
	 * 2 = interest
	 * 3 = role
	 * 4 = withdraw limit
	 * 
	 * @param playerName
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<ArrayList<String>> getAllUserAccountsInfo(String playerName) throws SQLException {
		//the most terrifying query i've written yet
		String query = "SELECT Accounts.accountno, Accounts.balance, Accounts.interest, " +
				"AccountEntries.role, AccountEntries.withdrawlimit FROM AccountEntries INNER JOIN Accounts " +
				"WHERE AccountEntries.accountno=Accounts.accountno AND AccountEntries.user='" + playerName + "'";
		Connection conn = null;
		boolean isUser = false;
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			
			ArrayList<ArrayList<String>> crazyArray = new ArrayList<ArrayList<String>>();
			String accountno = "";
			String balance = "";
			String interest = "";
			String role = "";
			String withdrawlimit = "";
			if (!rs.next()) return null;
			do {
				ArrayList<String> smallerArray = new ArrayList<String>();
				accountno = String.valueOf(rs.getInt(1));
				balance = String.valueOf(rs.getDouble(2));
				interest = String.valueOf(rs.getDouble(3));
				role = rs.getString(4);
				withdrawlimit = String.valueOf(rs.getDouble(5));
				smallerArray.add(accountno);
				smallerArray.add(balance);
				smallerArray.add(interest);
				smallerArray.add(role);
				smallerArray.add(withdrawlimit);
				crazyArray.add(smallerArray);
			} while (rs.next());
			conn.commit();
			conn.close();
			return crazyArray; //this is insane
		} catch (SQLException e) {
			conn.rollback();
			conn.close();
			e.printStackTrace();
			
			throw new SQLException();
		}
	}
	
	/**
	 * Return specifiable number pairings of wallet balance and wallet users in descending order.
	 * 
	 * @param count - how many pairings to get
	 * @return
	 * @throws SQLException 
	 */
	public static HashMap<String, Double> getTopWalletPairings(int count) throws SQLException {
		HashMap<String, Double> map = new HashMap<String, Double>();
		double[] balances = SQLUtil.getTopDoubles("balance", EconomosConstants.DB_WALLET_TABLENAME, "balance", count);
		String[] users = SQLUtil.getTopStrings("user", EconomosConstants.DB_WALLET_TABLENAME, "balance", count);
		for (int i=0; i < count; i++) {
			map.put(users[i], balances[i]);
		}
		return map;
	}
	
	public static double getAccountInterest(String accountno) throws NumberFormatException, SQLException {
		return Double.parseDouble(SQLUtil.getInt("interest", EconomosConstants.DB_ACCOUNTS_TABLENAME, "accountno", accountno));
	}
	
	public static void setAccountInterest(String accountno, Double interest) throws SQLException {
		String execute = "UPDATE " + EconomosConstants.DB_ACCOUNTS_TABLENAME +
				" SET interest=" + interest + " WHERE accountno=" + accountno;
		SQLUtil.transactUpdate(execute);
	}
	
	public static String getAccountUserRole(String accountno, String playerName) throws SQLException {
		return SQLUtil.getString("SELECT role FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME +
				" WHERE accountno=" + accountno + " AND user='" + playerName + "'");
	}
	
	public static void setAccountUserRole(String player, String accountno, String role) throws SQLException {
		String execute = "UPDATE " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME +
				" SET role='" + role + "' WHERE accountno=" + accountno + " AND user='" + player + "'";
		SQLUtil.transactUpdate(execute);
	}
	
	public static double getAccountUserWithdrawLimit(String accountno, String playerName) throws NumberFormatException, SQLException {
		return Double.parseDouble(SQLUtil.getString("SELECT withdrawlimit FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME +
				" WHERE accountno=" + accountno + " AND user='" + playerName + "'"));
	}
	
	public static void setAccountUserWithdrawLimit(String player, String accountno, Double withdrawlimit) throws SQLException {
		String execute = "UPDATE " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME +
				" SET withdrawlimit=" + withdrawlimit + " WHERE accountno=" + accountno + " AND user='" + player + "'";
		SQLUtil.transactUpdate(execute);
	}
	
	public static double getAccountBalance(String accountno) throws NumberFormatException, SQLException {
		return Double.parseDouble(SQLUtil.getInt("balance", EconomosConstants.DB_ACCOUNTS_TABLENAME, "accountno", accountno));
	}
	
	public static void setAccountBalance(String accountNo, Double value) throws SQLException {
		String execute = "UPDATE " + EconomosConstants.DB_ACCOUNTS_TABLENAME +
				" SET balance=" + value + " WHERE accountno=" + accountNo + "";
		SQLUtil.transactUpdate(execute);
	}
	
	public static void incrementAccountBalance(String accountNo, Double value) throws NumberFormatException, SQLException {
		value += getAccountBalance(accountNo);
		String execute = "UPDATE " + EconomosConstants.DB_ACCOUNTS_TABLENAME +
				" SET balance=" + value + " WHERE accountno=" + accountNo + "";
		SQLUtil.transactUpdate(execute);
	}
	
	public static void decrementAccountBalance(String accountNo, Double value) throws NumberFormatException, SQLException {
		value = getAccountBalance(accountNo) - value;
		String execute = "UPDATE " + EconomosConstants.DB_ACCOUNTS_TABLENAME +
				" SET balance=" + value + " WHERE accountno=" + accountNo + "";
		SQLUtil.transactUpdate(execute);
	}
	
	public static double getWalletBalance(String playerName) throws NumberFormatException, SQLException {
		return Double.parseDouble(SQLUtil.getInt("balance", EconomosConstants.DB_WALLET_TABLENAME, "user", playerName));
	}
	
	public static void setWalletBalance(String playerName, Double value) throws SQLException {
		String execute = "UPDATE " + EconomosConstants.DB_WALLET_TABLENAME +
				" SET balance=" + value + " WHERE user='" + playerName + "'";
		SQLUtil.transactUpdate(execute);
	}
	
	public static void incrementWalletBalance(String playerName, Double value) throws NumberFormatException, SQLException {
		value += getWalletBalance(playerName);
		String execute = "UPDATE " + EconomosConstants.DB_WALLET_TABLENAME +
				" SET balance=" + value + " WHERE user='" + playerName + "'";
		SQLUtil.transactUpdate(execute);
	}
	
	public static void decrementWalletBalance(String playerName, Double value) throws NumberFormatException, SQLException {
		value = getWalletBalance(playerName) - value;
		String execute = "UPDATE " + EconomosConstants.DB_WALLET_TABLENAME +
				" SET balance=" + value + " WHERE user='" + playerName + "'";
		SQLUtil.transactUpdate(execute);
	}
	
}
