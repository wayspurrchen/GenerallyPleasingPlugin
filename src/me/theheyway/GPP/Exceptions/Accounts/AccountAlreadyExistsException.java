package me.theheyway.GPP.Exceptions.Accounts;

import org.bukkit.ChatColor;

public class AccountAlreadyExistsException extends AccountException {
	
	public AccountAlreadyExistsException(String accountno) {
		super(ChatColor.DARK_RED + "Account " + ChatColor.RED + accountno + ChatColor.DARK_RED + " already exists.");
	}

}
