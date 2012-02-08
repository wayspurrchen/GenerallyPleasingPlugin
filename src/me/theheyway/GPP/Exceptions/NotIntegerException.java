package me.theheyway.GPP.Exceptions;

import org.bukkit.ChatColor;

public class NotIntegerException extends InvalidArgumentException {
	
	public NotIntegerException() {
		super("Argument is not an integer value.");
	}
	
	public NotIntegerException(String message) {
		super(message);
	}

}
