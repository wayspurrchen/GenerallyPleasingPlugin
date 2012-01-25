package me.theheyway.GPP.Exceptions.Accounts;

import org.bukkit.ChatColor;

public class AccountNotEnoughFundsException extends AccountException {
	
	public AccountNotEnoughFundsException(double value, String accountno) {
		super(ChatColor.DARK_RED + "You do not have " + ChatColor.RED + value + ChatColor.DARK_RED + " in account " + ChatColor.RED + accountno + ChatColor.DARK_RED + ".");
	}
	
	public AccountNotEnoughFundsException(String message) {
		super(message);
	}

}
