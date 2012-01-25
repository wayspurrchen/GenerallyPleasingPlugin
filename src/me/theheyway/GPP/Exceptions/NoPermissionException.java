package me.theheyway.GPP.Exceptions;

import org.bukkit.ChatColor;

public class NoPermissionException extends GPPException {
	
	public NoPermissionException() {
		super(ChatColor.DARK_RED + "You do not have permissions for that command.");
	}
	
	public NoPermissionException(String message) {
		super(message);
	}

}
