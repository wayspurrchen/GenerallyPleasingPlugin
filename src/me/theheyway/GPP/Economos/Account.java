package me.theheyway.GPP.Economos;

import java.sql.SQLException;

/**
 * This object represents the direct relationship between an Account and an AccountEntry. It stores the account number,
 * the user using it, their role for it, and their withdraw limit. Accounts can also access their own internal data about
 * balance and interest, but this is not stored to the JVM memory--it has to be queried from the MySQL database.
 * 
 * This class does not deal with other users who use the same account number.
 * 
 * @author Way
 *
 */
public class Account {

	private String accountNumber;
	private String playerName;
	private String role;
	private double withdrawLimit;
	
	protected Account(String playerName, String accountNumber) throws NumberFormatException, SQLException {
		this.playerName = playerName;
		this.accountNumber = accountNumber;
		updateRole();
		updateWithdrawLimit();
	}
	
	/**
	 * Update this Account's role variable in accordance with current MySQL data.
	 * @throws SQLException 
	 */
	public void updateRole() throws SQLException {
		role = AccountUtil.getAccountUserRole(accountNumber, playerName);
	}
	
	/**
	 * Update this Account's withdrawlimit variable in accordance with current MySQL data.
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public void updateWithdrawLimit() throws NumberFormatException, SQLException {
		withdrawLimit = AccountUtil.getAccountUserWithdrawLimit(accountNumber, playerName);
	}
	
	public double getWithdrawLimit() {
		return withdrawLimit;
	}
	
	public String getRole() {
		return role;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public double getInterest() throws NumberFormatException, SQLException {
		return AccountUtil.getAccountInterest(accountNumber);
	}
	
	public void setInterest(double interest) throws SQLException {
		AccountUtil.setAccountInterest(accountNumber, interest);
	}
	
	public double getBalance() throws NumberFormatException, SQLException {
		return AccountUtil.getAccountBalance(accountNumber);
	}
	
	public void setBalance(double value) throws SQLException {
		AccountUtil.setAccountBalance(accountNumber, value);
	}
	
}
