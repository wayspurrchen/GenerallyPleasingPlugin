package me.theheyway.GPP.Exceptions;

import me.theheyway.GPP.Colors;

import org.bukkit.ChatColor;

public class FrozenException extends GPPException {

	public FrozenException() {
		super("You are frozen and cannot teleportation commands.");
	}
	
	public FrozenException(String message) {
		super(message);
	}
	
}