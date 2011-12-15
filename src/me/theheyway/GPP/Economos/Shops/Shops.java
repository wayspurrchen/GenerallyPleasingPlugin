package me.theheyway.GPP.Economos.Shops;

import static me.theheyway.GPP.Economos.CashIn.CashInConstants.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.theheyway.GPP.Economos.Economos;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Shops {
	
	static File shops_config_file = new File(CASHIN_CONFIG_PATH);
	static FileConfiguration shops_config = null;
	
	public ShopsCommands commands;
	public ShopsConstants constants;
	public ShopManager shopMan; //Shop Man! To the rescue!
	
	private Economos plugin;
	
	public Shops(Economos economos) {
		plugin = economos;
		setup();
	}

	private void setup() {
		
		reloadConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		constants = new ShopsConstants();
		commands = new ShopsCommands(plugin);
	}
	
	public void reloadConfig() {
		
		shops_config = YamlConfiguration.loadConfiguration(shops_config_file);
		
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource(CASHIN_INTERNAL_CONFIG);
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        
	        shops_config.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getConfig() {
		if (shops_config == null) {
			reloadConfig();
		}
		return shops_config;
	}
	
	public void saveConfig() {
		try {
	        shops_config.save(CASHIN_CONFIG_PATH);
	    } catch (IOException ex) {
	    	Economos.logger.log(Level.SEVERE, "Could not save config to " + shops_config_file, ex);
	    }
	}

}
		
