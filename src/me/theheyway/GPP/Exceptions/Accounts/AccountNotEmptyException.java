package me.theheyway.GPP.Exceptions.Accounts;

import org.bukkit.ChatColor;

public class AccountNotEmptyException extends AccountException {
	
	public AccountNotEmptyException(String accountno) {
		super(ChatColor.DARK_RED + "You cannot close account " + ChatColor.RED + accountno + ChatColor.DARK_RED + " while it still contains funds.");
	}

}
