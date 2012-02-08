package me.theheyway.GPP.Exceptions;

import me.theheyway.GPP.Colors;

import org.bukkit.ChatColor;

public class GPPException extends Exception {

	public GPPException() {
		super();
	}
	
	public GPPException(String message) {
		super(Colors.ERROR + message);
	}
	
}
