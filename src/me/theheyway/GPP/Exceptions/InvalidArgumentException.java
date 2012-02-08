package me.theheyway.GPP.Exceptions;

import me.theheyway.GPP.Colors;

import org.bukkit.ChatColor;

public class InvalidArgumentException extends GPPException {
	
	public InvalidArgumentException() {
		super("One or more arguments is invalid.");
	}
	
	public InvalidArgumentException(String message) {
		super(message);
	}

}
