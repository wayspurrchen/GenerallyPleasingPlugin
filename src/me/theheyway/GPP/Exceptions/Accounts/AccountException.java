package me.theheyway.GPP.Exceptions.Accounts;

import me.theheyway.GPP.Exceptions.GPPException;

import org.bukkit.ChatColor;

public class AccountException extends GPPException {
	
	public AccountException() {
		super();
	}
	
	public AccountException(String message) {
		super(ChatColor.DARK_RED + message);
	}

}
