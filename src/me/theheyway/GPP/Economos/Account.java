package me.theheyway.GPP.Economos;

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
	
	protected Account(String playerName, String accountNumber) {
		this.playerName = playerName;
		this.accountNumber = accountNumber;
		updateRole();
		updateWithdrawLimit();
	}
	
	/**
	 * Update this Account's role variable in accordance with current MySQL data.
	 */
	public void updateRole() {
		role = AccountUtil.getAccountUserRole(accountNumber, playerName);
	}
	
	/**
	 * Update this Account's withdrawlimit variable in accordance with current MySQL data.
	 */
	public void updateWithdrawLimit() {
		withdrawLimit = AccountUtil.getAccountWithdrawLimit(accountNumber, playerName);
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
	
	public double getInterest() {
		return AccountUtil.getAccountInterest(accountNumber);
	}
	
	public double getBalance() {
		return AccountUtil.getAccountBalance(accountNumber);
	}
	
	public void setBalance(double value) {
		AccountUtil.setAccountBalance(accountNumber, value);
	}
	
}
