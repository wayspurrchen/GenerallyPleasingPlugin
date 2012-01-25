package me.theheyway.GPP.Overlord;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import me.theheyway.GPP.Constants;
import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Economos.EconomosConstants;
import me.theheyway.GPP.Places.PlacesCommands;
import me.theheyway.GPP.Util.SQLUtil;

public class Overlord {
	
	private GPP plugin;
	
	//Command Managers
	public OverlordCommands commands;
	me.theheyway.GPP.Places.PlacesCommands ports_commands;
	me.theheyway.GPP.Overlord.Cruelty cruelty_commands;
	me.theheyway.GPP.Overlord.Inquisitor inquisitor_commands;
	
	public Overlord(GPP plugin) {
		this.plugin = plugin;
		
		commands = new me.theheyway.GPP.Overlord.OverlordCommands(plugin, this);
		cruelty_commands = new me.theheyway.GPP.Overlord.Cruelty(plugin, this);
		inquisitor_commands = new me.theheyway.GPP.Overlord.Inquisitor(plugin, this);

		//Cruelty - punishing players for being bad
		plugin.getCommand("freeze").setExecutor(cruelty_commands);
		
		//Overlord - punishing players for being bad
		plugin.getCommand("clear").setExecutor(inquisitor_commands);
		plugin.getCommand("strip").setExecutor(inquisitor_commands);
		
	}

}
