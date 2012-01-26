package me.theheyway.GPP.Exceptions;

import org.bukkit.ChatColor;

public class FrozenException extends Exception {

	public FrozenException() {
		super();
	}
	
	public FrozenException(String message) {
		super(ChatColor.DARK_RED + message);
	}
	
}