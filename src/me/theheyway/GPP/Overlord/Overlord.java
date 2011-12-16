package me.theheyway.GPP.Overlord;

import me.theheyway.GPP.GPP;

public class Overlord {
	
	private GPP plugin;
	
	//Command Managers
	me.theheyway.GPP.Overlord.General general_commands;
	me.theheyway.GPP.Overlord.Ports ports_commands;
	me.theheyway.GPP.Overlord.Cruelty cruelty_commands;
	me.theheyway.GPP.Overlord.Inquisitor inquisitor_commands;
	
	public Overlord(GPP plugin) {
		this.plugin = plugin;
		
		general_commands = new me.theheyway.GPP.Overlord.General(plugin, this);
		ports_commands = new me.theheyway.GPP.Overlord.Ports(plugin, this);
		cruelty_commands = new me.theheyway.GPP.Overlord.Cruelty(plugin, this);
		inquisitor_commands = new me.theheyway.GPP.Overlord.Inquisitor(plugin, this);
		
		//Core GPP commands
		plugin.getCommand("gpp").setExecutor(general_commands);
		plugin.getCommand("cr").setExecutor(general_commands);
		plugin.getCommand("point").setExecutor(general_commands);
		plugin.getCommand("cm").setExecutor(general_commands);
		plugin.getCommand("test").setExecutor(general_commands);
		plugin.getCommand("flipcoin").setExecutor(general_commands);
		plugin.getCommand("flip").setExecutor(general_commands);
		plugin.getCommand("heal").setExecutor(general_commands);
				
		//Ports - spawn, homes, warps, teleport commands
		plugin.getCommand("home").setExecutor(ports_commands);
		plugin.getCommand("jumpto").setExecutor(ports_commands);
		plugin.getCommand("put").setExecutor(ports_commands);
		plugin.getCommand("spawn").setExecutor(ports_commands);
		plugin.getCommand("sethome").setExecutor(ports_commands);
		plugin.getCommand("setspawn").setExecutor(ports_commands);
		plugin.getCommand("summon").setExecutor(ports_commands);
		plugin.getCommand("tp").setExecutor(ports_commands);

		//Cruelty - punishing players for being bad
		plugin.getCommand("freeze").setExecutor(cruelty_commands);
		
		//Overlord - punishing players for being bad
		plugin.getCommand("clear").setExecutor(inquisitor_commands);
		plugin.getCommand("strip").setExecutor(inquisitor_commands);
	}

}
