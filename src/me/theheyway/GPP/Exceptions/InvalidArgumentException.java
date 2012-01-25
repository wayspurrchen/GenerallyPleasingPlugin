package me.theheyway.GPP.Exceptions;

import org.bukkit.ChatColor;

public class InvalidArgumentException extends GPPException {
	
	public InvalidArgumentException() {
		super(ChatColor.DARK_RED + "One or more arguments is invalid.");
	}
	
	public InvalidArgumentException(String message) {
		super(message);
	}

}
