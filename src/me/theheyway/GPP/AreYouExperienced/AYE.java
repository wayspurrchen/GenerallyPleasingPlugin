package me.theheyway.GPP.AreYouExperienced;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Listeners.GPPPlayerListener;
import me.theheyway.GPP.Listeners.GPPBlockListener;

import static me.theheyway.GPP.AreYouExperienced.Constants.*;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/*
 * NOTE ABOUT AYE: It's designed to increase player EXP, HOWEVER, there is no graphical change for when a player
 * gains EXP. This is UNACCEPTABLE. There is a workaround, which involves dropping experience orbs on the player's location,
 * but they can only be dropped if their experience value is at least an integer value of 1, which is 1/20th of a whole level
 * of experience. Therefore, local variables need to store how much of 1/20th of a level of EXP is gained in order to know
 * when to drop an experience orb. Maybe this should be stored to flatfiles, but PROBABLY NOT because that little amount
 * of exp is negligible.
 * 
 */

public class AYE {
	Constants constants;
	
	private GPP plugin;
	
	static File aye_config_file = new File(AYE_CONFIG_PATH);
	static FileConfiguration aye_config = null;
	
	public AYE(GPP plugin) {
		this.plugin = plugin;
		
		//Set up module config
		reloadConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		constants = new Constants(plugin, this);
		
	}
	
	public void reloadConfig() {
		
		aye_config = YamlConfiguration.loadConfiguration(aye_config_file);
		
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource(AYE_INTERNAL_CONFIG);
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        
	        aye_config.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getConfig() {
		if (aye_config == null) {
			reloadConfig();
		}
		return aye_config;
	}
	
	public void saveConfig() {
		try {
	        aye_config.save(AYE_CONFIG_PATH);
	    } catch (IOException ex) {
	    	GPP.logger.log(Level.SEVERE, "Could not save config to " + aye_config_file, ex);
	    }
	}
		
}