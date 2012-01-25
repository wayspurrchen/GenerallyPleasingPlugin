package me.theheyway.GPP.Exceptions;

import org.bukkit.ChatColor;

public class NotDoubleException extends InvalidArgumentException {
	
	public NotDoubleException() {
		super(ChatColor.DARK_RED + "Argument is not a numeric point value.");
	}
	
	public NotDoubleException(String message) {
		super(message);
	}

}
