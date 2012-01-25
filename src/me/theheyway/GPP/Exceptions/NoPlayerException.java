package me.theheyway.GPP.Exceptions;

import org.bukkit.ChatColor;

public class NoPlayerException extends GPPException {
	
	public NoPlayerException() {
		super(ChatColor.DARK_RED + "Could not find a player by that name.");
	}
	
	public NoPlayerException(String message) {
		super(message);
	}

}
