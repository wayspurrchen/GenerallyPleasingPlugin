package me.theheyway.GPP;

import java.util.logging.Logger;

import me.theheyway.GPP.AreYouExperienced.AYE;
import me.theheyway.GPP.Economos.Economos;
import me.theheyway.GPP.Listeners.GPPBlockListener;
import me.theheyway.GPP.Listeners.GPPEntityListener;
import me.theheyway.GPP.Listeners.GPPPlayerListener;
import me.theheyway.GPP.Overlord.Overlord;
import me.theheyway.GPP.Util.TimerUtil;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GPP extends JavaPlugin {
	
	public static Server server; //Test commit by crogeniks yo
	
	me.theheyway.GPP.Constants constants;
	
	//Command Managers
	me.theheyway.GPP.Overlord.Overlord overlord;
	me.theheyway.GPP.AreYouExperienced.AYE aye;
	me.theheyway.GPP.Economos.Economos economos;
	
	//Listeners
	me.theheyway.GPP.Listeners.GPPEntityListener entityListener = new GPPEntityListener(this);
	me.theheyway.GPP.Listeners.GPPPlayerListener playerListener = new GPPPlayerListener(this); //lolz gppp
	me.theheyway.GPP.Listeners.GPPBlockListener blockListener = new GPPBlockListener(this); // they make funny sounding consonants--GPPB!!! GPPB gppbbbgplfhth
	
	public static final Logger logger = Logger.getLogger("Minecraft");
	
	public GPP() {
		
	}
	
	public void onEnable() {
		//A friendly little message
		PluginDescriptionFile pdfFile = this.getDescription();
		logger.info(pdfFile.getFullName()+" enabled.");
		
		//Set up PluginManager and event handlers
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
		
		server = getServer();
		
		constants = new Constants(this);
		
		//Set up module config
		reloadConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		//For time management
		new TimerUtil();
		
		overlord = new Overlord(this);
		
		if (Constants.AREYOUEXPERIENCED_ENABLED) {
			aye = new AYE(this);
			GPP.consoleInfo("[GPP] AreYouExperienced module enabled.");
		} else {
			GPP.consoleInfo("[GPP] AreYouExperienced module disabled.");
		}
		
		if (Constants.ECONOMOS_ENABLED) {
			GPP.consoleInfo("[GPP] Economos module enabled.");
			economos = new Economos(this);
		} else {
			GPP.consoleInfo("[GPP] Economos module disabled.");
		}
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