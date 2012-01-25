package me.theheyway.GPP.Exceptions;

import org.bukkit.ChatColor;

public class GPPException extends Exception {

	public GPPException() {
		super();
	}
	
	public GPPException(String message) {
		super(ChatColor.DARK_RED + message);
	}
	
}
