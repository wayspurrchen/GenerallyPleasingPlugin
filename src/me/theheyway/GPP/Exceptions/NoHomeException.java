package me.theheyway.GPP.Exceptions;

public class NoHomeException extends GPPException {

	public NoHomeException() {
		super("You don't have a home in this world. Use /sethome to set one.");
	}
	
	public NoHomeException(String message) {
		super(message);
	}
	
}
