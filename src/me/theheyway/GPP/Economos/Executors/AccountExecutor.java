package me.theheyway.GPP.Economos.Executors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import me.theheyway.GPP.Executor;
import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Secretary;
import me.theheyway.GPP.Strings;
import me.theheyway.GPP.Economos.Account;
import me.theheyway.GPP.Economos.AccountUtil;
import me.theheyway.GPP.Economos.Accountant;
import me.theheyway.GPP.Economos.EconomosConstants;
import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Exceptions.NoMorePagesException;
import me.theheyway.GPP.Exceptions.NoPlayerException;
import me.theheyway.GPP.Exceptions.Accounts.AccountCreationFailedException;
import me.theheyway.GPP.Util.Arguments;
import me.theheyway.GPP.Util.SQLUtil;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class AccountExecutor extends Executor {
	
	/**
	 * Creates a new account, properly adding Account objects to any online players and creating an Account and AccountEntry pair
	 * for particular player. If playerName passed is "" or null, then a lone account is created with no owners.
	 * 
	 * @param creator
	 * @param playerName
	 * @param accountno
	 * @return
	 * @throws SQLException 
	 */
	public static void createAccount(Player creator, String playerName, String accountno) throws GPPException, SQLException {
		
		if (playerName == null || playerName.isEmpty()) {
			AccountUtil.createAccount(accountno, 0);
			creator.sendMessage(ChatColor.YELLOW + "Account " + ChatColor.WHITE + accountno 
					+ ChatColor.YELLOW + " without owner created.");
		} else {
		
			AccountUtil.createAccountAndAccountEntry(playerName, accountno, 0, 0);
			
			Player player = GPP.server.getPlayerExact(playerName);
			if (player!=null) {
				Accountant accountant = AccountUtil.getAccountant(playerName);
				if (accountant!=null) {
					accountant.addAccount(accountno);
					accountant.setActiveAccount(accountno);
				}
			}
			
			//Message confirmation of success
			if (creator.getName().equals(playerName)) {
				creator.sendMessage(ChatColor.YELLOW + "Account " + ChatColor.WHITE + accountno 
								+ ChatColor.YELLOW + " created.");
			} else {
				creator.sendMessage(ChatColor.YELLOW + "Account " + ChatColor.WHITE + accountno 
						+ ChatColor.YELLOW + " for " + ChatColor.WHITE + playerName + ChatColor.YELLOW + " created.");
			}
		}
		
	}
	
	public static void closeAccount(Player closer, String accountno) throws GPPException, SQLException {
		/*
		 * This needs to check for whether any players are online who own the account and delete their Account objects
		 * from their ArrayList<Account> in their Accountant object.
		 */
		ArrayList<Player> players = AccountUtil.getAccountOnlineUsers(accountno);
		if (!players.isEmpty()) {
			for (int i=0; i < players.size(); i++) {
				Accountant accountant = AccountUtil.getAccountant(players.get(i));
				accountant.removeAccount(accountno);
			}
		}
		
		AccountUtil.deleteAccount(accountno);
		closer.sendMessage(ChatColor.YELLOW + "Account " + ChatColor.WHITE + accountno 
				+ ChatColor.YELLOW + " closed.");
	}
	
	public static void displayWalletBalance(Player executor, String targetName) throws NumberFormatException, SQLException {
		Accountant accountant = AccountUtil.getAccountant(targetName);
		if (accountant!=null) {
			double bal = AccountUtil.getAccountant(targetName).getWalletBalance();
			executor.sendMessage(ChatColor.GOLD + targetName + "'s " + EconomosConstants.WALLET_NAME);
			executor.sendMessage(ChatColor.GOLD + "Balance: " + ChatColor.WHITE + bal + ChatColor.YELLOW + " " + EconomosConstants.CURRENCY_NAME_PLURAL);
			executor.sendMessage(ChatColor.YELLOW + Strings.DIVIDER);
		} else {
			//TODO: Query DB for offline player
			
		}
	}
	
	/**
	 * Displays a paginated list of accounts used by a specified player to a requesting player. Lists the account number, the using player's role,
	 * the account balance, their withdraw limits, and the account's balance.
	 * 
	 * @param player - player requesting information
	 * @param targetPlayer - player whose accounts are being queried
	 * @throws NumberFormatException
	 * @throws SQLException
	 * @throws NoMorePagesException
	 */
	public static void displayAccountsList(Player player, String targetPlayer) throws NumberFormatException, SQLException, NoMorePagesException {
		//Create Secretary to display paginated information
		Secretary secretary = new Secretary(player);
		//Grab player's accountant
		Accountant accountant = AccountUtil.getAccountant(targetPlayer);
		//If the accountant is not null (exists), use the accountant to grab a list of the player's accounts
		if (accountant!=null) {
			
			if (accountant.playerName().equals(player.getName())) secretary.addLine(ChatColor.GOLD + "Your Accounts");
			else secretary.addLine(ChatColor.GOLD + targetPlayer + "'s Accounts");
			
			for (int i=0; i <accountant.accountCount(); i++) {
				Account account = accountant.getAccounts().get(i);
				
				double withdrawlimit = account.getWithdrawLimit();
				String withdrawString = "";
				if (withdrawlimit!=0) {
					withdrawString = "" + withdrawlimit + ChatColor.YELLOW + EconomosConstants.CURRENCY_NAME_PLURAL;
				} else {
					withdrawString = "Unlimited";
				}
				
				String string = ChatColor.GOLD + " - " + ChatColor.YELLOW + account.getAccountNumber() + ChatColor.GOLD + " ("
						+ ChatColor.WHITE + account.getRole() + ChatColor.GOLD + "): "
						+ ChatColor.WHITE + account.getBalance() + ChatColor.YELLOW +  ChatColor.GOLD + " [" + ChatColor.WHITE + withdrawString + ChatColor.GOLD + "]"
						+ " x " + ChatColor.WHITE + account.getInterest() + ChatColor.YELLOW + " per cycle";
				secretary.addLine(string);
			}
			secretary.addLine(ChatColor.YELLOW + Strings.DIVIDER);
			
		//If the accountant doesn't exist, query the MySQL DB directly
		} else {
			ArrayList<ArrayList<String>> array = AccountUtil.getAllUserAccountsInfo(targetPlayer);
			if (!array.isEmpty()) {
				secretary.addLine(ChatColor.GOLD + targetPlayer + "'s Accounts");
				for (int i=0; i < array.size(); i++) {
					String account = array.get(i).get(0);
					String balance = array.get(i).get(1);
					String interest = array.get(i).get(2);
					String role = array.get(i).get(3);
					String withdrawlimit = array.get(i).get(4);
					
					if (withdrawlimit.equalsIgnoreCase("0")) { // ignore case of 0? that's silly!
						withdrawlimit = "Unlimited";
					} else {
						withdrawlimit = "" + withdrawlimit + ChatColor.YELLOW + EconomosConstants.CURRENCY_NAME_PLURAL;
					}
					
					String string = ChatColor.GOLD + " - " + ChatColor.YELLOW + account + ChatColor.GOLD + " ("
							+ ChatColor.WHITE + role + ChatColor.GOLD + "): "
							+ ChatColor.WHITE + balance + ChatColor.YELLOW +  ChatColor.GOLD + " [" + ChatColor.WHITE + withdrawlimit + ChatColor.GOLD + "]"
							+ " x " + ChatColor.WHITE + interest + ChatColor.YELLOW + " per cycle";
					secretary.addLine(string);
				}
			secretary.addLine(ChatColor.YELLOW + Strings.DIVIDER);
			} else secretary.addLine(ChatColor.WHITE + targetPlayer + ChatColor.GOLD + " has no accounts.");
		}
		
		secretary.sendPage();
	}
	
	/**
	 * Displays information about all of a player's accounts. This must deal with the account number string as opposed
	 * to an Account object because it needs to query additional information about the account's other users, as well
	 * as that an administrator may need to access the account of another player for information.
	 * 
	 * 
	 * @param accountNumber
	 * @param player
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public static void displayAccountInfo(String accountno, Player player) throws NumberFormatException, SQLException {
		/*
		 * Schema returned:
		 * 
		 * 1 - id (integer)
		 * 2 - accountno (integer)
		 * 3 - user (string)
		 * 4 - role (string)
		 * 5 - withdrawlimit (double)
		 * 
		 */
		String query = "SELECT * FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME +
				" WHERE accountno=" + accountno;
		Connection conn = null;
		ArrayList<String> users = new ArrayList<String>();
		ArrayList<String> roles = new ArrayList<String>();
		ArrayList<Double> withdrawlimits = new ArrayList<Double>();
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				users.add(String.valueOf(rs.getString(3)));
				roles.add(String.valueOf(rs.getString(4)));
				withdrawlimits.add(rs.getDouble(5));
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
		}
		
		double bal = AccountUtil.getAccountBalance(accountno);
		double interest = AccountUtil.getAccountInterest(accountno);
		player.sendMessage(ChatColor.GOLD + "Account #" + ChatColor.YELLOW + accountno);
		player.sendMessage(ChatColor.GOLD + "Balance: " + ChatColor.WHITE + bal + ChatColor.YELLOW + " " + EconomosConstants.CURRENCY_NAME_PLURAL);
		player.sendMessage(ChatColor.GOLD + "Interest: " + ChatColor.WHITE + interest + ChatColor.YELLOW + " per cycle");
		int owner = -1;
		/*int owner = users.indexOf("owner");
		player.sendMessage(ChatColor.GOLD + " - " + ChatColor.WHITE + users.get(owner) + ChatColor.GOLD + " (" +
				ChatColor.YELLOW + roles.get(owner) + ChatColor.GOLD + ") " + ChatColor.YELLOW + " | " +
				ChatColor.GOLD + " Withdraw Limit: " + ChatColor.WHITE + " Unlimited");*/
		player.sendMessage(ChatColor.GOLD + "Account Holder(s):");
		if (users.size()>0) {
			for (int i=0; i < users.size(); i++) {
				if (i!=owner) {
					String string = ChatColor.GOLD + " - " + ChatColor.WHITE + users.get(i) + ChatColor.GOLD + " (" +
							ChatColor.YELLOW + roles.get(i) + ChatColor.GOLD + ")" + ChatColor.YELLOW + " || " +
							ChatColor.GOLD + "Withdraw Limit: " + ChatColor.WHITE;
					if (withdrawlimits.get(i)!=0) {
						string += withdrawlimits.get(i);
					} else {
						string += "Unlimited";
					}
					player.sendMessage(string);
				}
			}
		} else {
			player.sendMessage(ChatColor.GOLD + " - " + ChatColor.WHITE + "None");
		}
		player.sendMessage(ChatColor.YELLOW + Strings.DIVIDER);
	}
	
	public static void displayAccountBalance(String accountNumber, Player player) throws NumberFormatException, SQLException {
		double bal = AccountUtil.getAccountBalance(accountNumber);
		player.sendMessage(ChatColor.GOLD + "Account: " + ChatColor.WHITE + accountNumber);
		player.sendMessage(ChatColor.GOLD + "Balance: " + ChatColor.WHITE + bal + ChatColor.YELLOW + " " + EconomosConstants.CURRENCY_NAME_PLURAL);
		player.sendMessage(ChatColor.YELLOW + Strings.DIVIDER);
	}
	
	public static void transferFunds(Player transferrer, String fromAccount, String toAccount, double value) throws NumberFormatException, SQLException {
		AccountUtil.incrementAccountBalance(fromAccount, -value);
		AccountUtil.incrementAccountBalance(toAccount, value);
		transferrer.sendMessage(ChatColor.YELLOW + "You transferred " + ChatColor.WHITE + value + " " + EconomosConstants.CURRENCY_NAME_PLURAL
				+ ChatColor.YELLOW + " from account " + ChatColor.WHITE + fromAccount + ChatColor.YELLOW + " to account "
				+ ChatColor.WHITE + toAccount + ChatColor.YELLOW + ".");
		ArrayList<String> receivers = AccountUtil.getAccountUsers(toAccount);
		for (int i=0; i < receivers.size(); i++) {
			Player receiverPlayer = GPP.server.getPlayer(receivers.get(i));
			if (GPP.server.matchPlayer(receivers.get(i))!=null) {
				receiverPlayer.sendMessage(ChatColor.WHITE + transferrer.getName() + ChatColor.YELLOW + " transferred "
						+ ChatColor.WHITE + value + " " + EconomosConstants.CURRENCY_NAME_PLURAL + ChatColor.YELLOW
						+ " to account " + ChatColor.WHITE + toAccount + ChatColor.YELLOW + ".");
			}
		}
	}
	
	public static void setBalance(Player setter, String accountno, double value) throws SQLException {
		AccountUtil.setAccountBalance(accountno, value);
		setter.sendMessage(ChatColor.YELLOW + "You have set account " + ChatColor.WHITE + accountno + ChatColor.YELLOW + "'s balance to "
				+ ChatColor.WHITE + value + ChatColor.YELLOW + ".");
	}
	
	public static void depositFunds(Player depositer, String accountno, double value) throws NumberFormatException, SQLException {
		AccountUtil.incrementAccountBalance(accountno, value);
		AccountUtil.decrementWalletBalance(depositer.getName(), value);
		depositer.sendMessage(ChatColor.YELLOW + "You have deposited " + ChatColor.WHITE + value + ChatColor.YELLOW + " into account "
				+ ChatColor.WHITE + accountno + ChatColor.YELLOW + ".");
	}
	
	public static void withdrawFunds(Player withdrawer, String accountno, double value) throws NumberFormatException, SQLException {
		AccountUtil.decrementAccountBalance(accountno, value);
		AccountUtil.incrementWalletBalance(withdrawer.getName(), value);
		withdrawer.sendMessage(ChatColor.YELLOW + "You have withdrawn " + ChatColor.WHITE + value + ChatColor.YELLOW + " from account "
				+ ChatColor.WHITE + accountno + ChatColor.YELLOW + ".");
	}
	
}
