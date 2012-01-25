package me.theheyway.GPP.Exceptions.Accounts;

import org.bukkit.ChatColor;

public class AccountDoesNotExistException extends AccountException {
	
	public AccountDoesNotExistException(String accountno) {
		super(ChatColor.DARK_RED + "Account " + ChatColor.RED + accountno + ChatColor.DARK_RED + " does not exist.");
	}

}
