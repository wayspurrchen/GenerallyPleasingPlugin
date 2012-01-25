package me.theheyway.GPP.Exceptions.Accounts;

import org.bukkit.ChatColor;

public class AccountCreationFailedException extends AccountException {
	
	public AccountCreationFailedException(String accountno) {
		super(ChatColor.DARK_RED + "Account " + ChatColor.RED + accountno + ChatColor.DARK_RED + " creation failed. Contact an administrator.");
	}

}
