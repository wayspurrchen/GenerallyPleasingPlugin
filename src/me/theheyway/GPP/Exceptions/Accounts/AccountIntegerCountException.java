package me.theheyway.GPP.Exceptions.Accounts;

import me.theheyway.GPP.Exceptions.GPPException;

import org.bukkit.ChatColor;

public class AccountIntegerCountException extends AccountException {
	
	public AccountIntegerCountException() {
		super(ChatColor.DARK_RED + "Account number must be seven digits or less.");
	}
	
	public AccountIntegerCountException(String message) {
		super(ChatColor.DARK_RED + message);
	}

}
