package me.theheyway.GPP.Exceptions.Accounts;

import org.bukkit.ChatColor;

public class AccountNotOwnerException extends AccountException {
	
	public AccountNotOwnerException(String accountno) {
		super(ChatColor.DARK_RED + "You are not the owner of account " + ChatColor.RED + accountno + ChatColor.DARK_RED + ".");
	}

}
