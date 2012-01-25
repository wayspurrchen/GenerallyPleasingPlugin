package me.theheyway.GPP.Exceptions;

import org.bukkit.ChatColor;

public class NoMorePagesException extends GPPException {
	
	public NoMorePagesException() {
		super(ChatColor.DARK_RED + "No more pages.");
	}
	
	public NoMorePagesException(String message) {
		super(message);
	}

}
