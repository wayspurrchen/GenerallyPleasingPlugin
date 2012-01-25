package me.theheyway.GPP.Exceptions.Accounts;

import org.bukkit.ChatColor;

public class WalletNotEnoughFundsException extends AccountException {
	
	public WalletNotEnoughFundsException(double value) {
		super(ChatColor.DARK_RED + "You do not have " + ChatColor.RED + value + ChatColor.DARK_RED + " in your wallet.");
	}
	
	public WalletNotEnoughFundsException(String message) {
		super(message);
	}

}
