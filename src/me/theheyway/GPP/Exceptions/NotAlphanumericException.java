package me.theheyway.GPP.Exceptions;

import org.bukkit.ChatColor;

public class NotAlphanumericException extends InvalidArgumentException {
	
	public NotAlphanumericException() {
		super(ChatColor.DARK_RED + "Argument must be alphanumeric.");
	}
	
	public NotAlphanumericException(String message) {
		super(message);
	}

}
