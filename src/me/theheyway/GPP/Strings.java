package me.theheyway.GPP;

import org.bukkit.ChatColor;

public class Strings {
	public static final String DIVIDER = "------------------";
	public static final String NO_PERMISSION = ChatColor.DARK_RED + "You do not have permission to use that command.";
	
	public static String ACCOUNT_NOACCESS(String accountno) {
		return ChatColor.DARK_RED + "You do not have access to account " + ChatColor.RED + accountno + ChatColor.DARK_RED + ".";
	}
	
	public static String ACCOUNT_NONEXISTENT(String accountno) {
		return ChatColor.DARK_RED + "Account " + ChatColor.RED + accountno + ChatColor.DARK_RED + " does not exist.";
	}
	
	public static String ACCOUNT_NOTOWNER(String accountno) {
		return ChatColor.DARK_RED + "You are not the owner of account " + ChatColor.RED + accountno + ChatColor.DARK_RED + ".";
	}
}