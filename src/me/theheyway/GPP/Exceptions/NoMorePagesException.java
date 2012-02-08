package me.theheyway.GPP.Exceptions;

import me.theheyway.GPP.Colors;

import org.bukkit.ChatColor;

public class NoMorePagesException extends GPPException {
	
	public NoMorePagesException() {
		super("No more pages.");
	}
	
	public NoMorePagesException(String message) {
		super(message);
	}

}
