package me.theheyway.GPP.AreYouExperienced;

import java.util.logging.Logger;

import me.theheyway.AreYouExperienced.Listeners.AYEPlayerListener;
import me.theheyway.AreYouExperienced.Listeners.AYEBlockListener;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AYE extends JavaPlugin {
	//THIS IS ERROR RIDDEN--BEWARE
	
	public static Server server;
	public static Constants constants;
	
	//Command Managers
	me.theheyway.GPP.AreYouExperienced.General general_commands;
	
	//Listeners
	
	//NO LONGER VALID DURING MERGE INTO GPP--TO BE FIXED LATER
	
	//me.theheyway.GPP.Listeners.AYEPlayerListener playerListener = new AYEPlayerListener(this);
	//me.theheyway.GPP.Listeners.AYEBlockListener blockListener = new AYEBlockListener(this);
	
	public static final Logger logger = Logger.getLogger("Minecraft");
	
	
	public AYE() {
		
	}
	
	public void onEnable() {
		//A friendly little message
		PluginDescriptionFile pdfFile = this.getDescription();
		logger.info(pdfFile.getFullName()+" enabled.");
		
		//Set up PluginManager and event handlers
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
		
		server = getServer();
		constants = new Constants(this);
		
		general_commands = new me.theheyway.AreYouExperienced.General(this);
		
		//Set up module config
		reloadConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		//Core AYE commands
		getCommand("aye").setExecutor(general_commands);

	}
	
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		logger.info(pdfFile.getFullName()+" disabled.");
	}
	
	public static void consoleFine(String message) {
		logger.fine(message);
	}
	
	public static void consoleInfo(String message) {
		logger.info(message);
	}
	
	public static void consoleSevere(String message) {
		logger.severe(message);
	}
	
	public static void consoleWarn(String message) {
		logger.warning(message);
	}

	//Debug method
	/*public void outConfigMap() {
			Map<String,Object> configMap = getConfig().getAll();
			for (Map.Entry<String, Object> e : configMap.entrySet())
				logger.info(e.getKey() + ": " + e.getValue());
	}*/
		
}