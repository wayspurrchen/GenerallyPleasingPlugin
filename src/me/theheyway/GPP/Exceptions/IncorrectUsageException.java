package me.theheyway.GPP.Exceptions;

import me.theheyway.GPP.Colors;

import org.bukkit.ChatColor;

public class IncorrectUsageException extends GPPException {
	
	public IncorrectUsageException() {
		super("Incorrect usage.");
	}
	
	public IncorrectUsageException(String message) {
		super(message);
	}

}
