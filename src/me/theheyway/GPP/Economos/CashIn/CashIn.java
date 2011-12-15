package me.theheyway.GPP.Economos.CashIn;

import static me.theheyway.GPP.Economos.CashIn.CashInConstants.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Economos.Economos;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CashIn {
	
	static File cashin_config_file = new File(CASHIN_CONFIG_PATH);
	static FileConfiguration cashin_config = null;
	
	public CashInCommands commands;
	public CashInConstants constants;
	
	private GPP plugin;
	private Economos economos;
	
	public CashIn(GPP plugin, Economos economos) {
		this.plugin = plugin;
		this.economos = economos;
		
		reloadConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		constants = new CashInConstants();
		commands = new CashInCommands(plugin, economos);
	}
	
	public void reloadConfig() {
		
		cashin_config = YamlConfiguration.loadConfiguration(cashin_config_file);
		
	    // Look for defaults in the jar
	    InputStream defConfigStream = plugin.getResource(CASHIN_INTERNAL_CONFIG);
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        
	        cashin_config.setDefaults(defConfig);
	    }
	}
	
	public FileConfiguration getConfig() {
		if (cashin_config == null) {
			reloadConfig();
		}
		return cashin_config;
	}
	
	public void saveConfig() {
		try {
	        cashin_config.save(CASHIN_CONFIG_PATH);
	    } catch (IOException ex) {
	    	GPP.logger.log(Level.SEVERE, "Could not save config to " + cashin_config_file, ex);
	    }
	}

}
		
