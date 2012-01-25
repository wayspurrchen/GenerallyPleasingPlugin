package me.theheyway.GPP.Exceptions.Accounts;

import org.bukkit.ChatColor;

public class AccountRequiredException extends AccountException {
	
	public AccountRequiredException() {
		super(ChatColor.YELLOW + "You do not have any accounts yet. Use " + ChatColor.WHITE + 
				"/acc create <#>" + ChatColor.YELLOW + " to make one.");
	}
	
	public AccountRequiredException(String message) {
		super(message);
	}

}
