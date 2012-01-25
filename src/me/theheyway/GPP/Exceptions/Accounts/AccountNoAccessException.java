package me.theheyway.GPP.Exceptions.Accounts;

import org.bukkit.ChatColor;

public class AccountNoAccessException extends AccountException {
	
	public AccountNoAccessException(String accountno) {
		super(ChatColor.DARK_RED + "You do not have access to account " + ChatColor.RED + accountno + ChatColor.DARK_RED + ".");
	}

}
