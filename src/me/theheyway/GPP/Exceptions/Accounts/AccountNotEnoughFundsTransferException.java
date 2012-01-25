package me.theheyway.GPP.Exceptions.Accounts;

import me.theheyway.GPP.Economos.EconomosConstants;

import org.bukkit.ChatColor;

public class AccountNotEnoughFundsTransferException extends AccountException {
	
	public AccountNotEnoughFundsTransferException(double value, String fromAccount, String toAccount) {
		super(ChatColor.DARK_RED + "Account " + ChatColor.RED + fromAccount + ChatColor.DARK_RED
				+ " does not have " + ChatColor.RED + value + " " + EconomosConstants.CURRENCY_NAME_PLURAL
				+ ChatColor.DARK_RED + " to send to account " + ChatColor.RED + toAccount + ChatColor.DARK_RED + ".");
	}
	
	public AccountNotEnoughFundsTransferException(String message) {
		super(message);
	}

}
