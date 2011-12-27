package me.theheyway.GPP.Economos;

import java.util.ArrayList;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Util.SQLUtil;

/**
 * Accountant acts as the player's primary manager for dealing with all Account objects. It stores what Accounts a player has
 * access to, and most Account actions can be called from here or directly through the accounts ArrayList<Account>.
 * 
 * @author Way
 *
 */
public class Accountant {
	
	private Account activeAccount = null;
	private String playerName = null;
	
	//Focusing around this ArrayList of accounts would make life much easier!
	//Later note: it did!
	private ArrayList<Account> accounts = new ArrayList<Account>();
	
	Accountant(String playerName) {
		this.playerName = playerName;
		if (!hasWallet()) createWallet();
		
		ArrayList<String> accountNumbers = SQLUtil.getIntValues("accountno", EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME, "user", playerName, 1);
		for (int i=0; i < accountNumbers.size(); i++) {
			accounts.add(new Account(playerName, accountNumbers.get(i)));
		}
	}
	
	public String playerName() {
		return playerName;
	}
	
	protected void addAccount(String accountNumber) {
		if (AccountUtil.hasAccountNo(playerName, accountNumber)) { //make sure it actually exists before creating
			accounts.add(new Account(playerName,accountNumber));
		} else {
			GPP.consoleWarn("[GPP::ECONOMOS] Tried to create Account for " + playerName + "'s accountant, but no such account exists in MySQL database!");
		}
	}
	
	protected boolean removeAccount(Account account) {
		try {
			accounts.remove(account);
			return true;
		} catch (NullPointerException e) {
			GPP.consoleInfo("GPP Could not remove account; " + playerName() + "'s Accountant does not have this account.");
			return false;
		}
	}
	
	protected boolean removeAccount(String accountno) {
		try {
			accounts.remove(getAccountByNumber(accountno));
			return true;
		} catch (NullPointerException e) {
			GPP.consoleInfo("GPP Could not remove account; " + playerName() + "'s Accountant does not have this account.");
			return false;
		}
	}
	
	protected Account getAccountByNumber(String accountno) {
		for (int i=0; i < accounts.size(); i++) {
			Account acc = accounts.get(i);
			if (acc.getAccountNumber().equals(accountno)) return acc;
		}
		return null;
	}
	
	protected double getAccountInterest(String accountno) {
		return getAccountByNumber(accountno).getInterest();
	}
	
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	
	public int accountCount() {
		return accounts.size();
	}
	
	public double getAccountBalance(String accountno) {
		Account acc = getAccountByNumber(accountno);
		if (acc!=null) {
			return acc.getBalance();
		} else return -1.0;
	}
	
	public void setAccountBalance(String accountno, double value) {
		Account acc = getAccountByNumber(accountno);
		if (acc!=null) {
			acc.setBalance(value);
		}
	}
	
	/**
	 * Checks whether the player has an active account. If they do, it returns true. If they do not, it attempts to set
	 * an active account from the first account in the ArrayList<String> accounts and returns true if it was successful,
	 * false if not.
	 * 
	 * @return
	 */
	public boolean guaranteeActiveAccount() {
		if (!hasActiveAccount()) {
			return defaultActiveAccount();
		} else return true;
	}
	
	public boolean hasActiveAccount() {
		if (activeAccount!=null) {
			return true;
		} else return false;
	}
	
	public Account getActiveAccount() {
		return activeAccount;
	}
	
	public String getActiveAccountNumber() {
		return activeAccount.getAccountNumber();
	}

	public void setActiveAccount(String accountnumber) {
		if (hasAccountNo(accountnumber)) {
			activeAccount = getAccountByNumber(accountnumber);
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] " + playerName + " set account " + accountnumber + " to active account.");
		} else {
			if (EconomosConstants.VERBOSE) GPP.consoleInfo("[GPP::ECONOMOS] " + playerName + " tried to set active account to " + accountnumber + " but account was not found.");
		}
	}
	
	public boolean defaultActiveAccount() {
		if (!accounts.isEmpty()) {
			activeAccount = accounts.get(0);
			return true;
		} else {
			if (EconomosConstants.VERBOSE) GPP.consoleWarn("[GPP::ECONOMOS] Couldn't set default active account for " + playerName + "; player has no accounts.");
			return false;
		}
	}
	
	public boolean isOwner(String accountno) {
		Account acc = getAccountByNumber(accountno);
		if (acc.getRole().equals("owner")) return true;
		else return false;
	}
	
	public String getAccountUserRole(String accountno) {
		Account acc = getAccountByNumber(accountno);
		return acc.getRole();
	}
	
	public double getAccountWithdrawLimit(String accountno) {
		Account acc = getAccountByNumber(accountno);
		return acc.getWithdrawLimit();
	}
	
	public boolean hasAccountNo(String accountno) {
		Account acc = getAccountByNumber(accountno);
		if (acc!=null) return true;
		else return false;
	}
	

	
	/**
	 * Checks if player has a wallet account set up in the database.
	 * 
	 * @return true if a player has a wallet account, false if not
	 */
	public boolean hasWallet() {
		if (SQLUtil.anyQualifiedValueExists(EconomosConstants.DB_WALLET_TABLENAME, "user", playerName)) return true;
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
	
	public String getFirstOwnedAccount() {
		return SQLUtil.getInt("accountno", EconomosConstants.DB_ACCOUNTS_TABLENAME, "user", playerName);
	}
	
	public double getWalletBalance() {
		return Double.parseDouble(SQLUtil.getInt("balance", EconomosConstants.DB_WALLET_TABLENAME, "user", playerName));
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
