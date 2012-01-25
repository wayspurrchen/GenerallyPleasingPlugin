package me.theheyway.GPP;

import java.sql.SQLException;

import me.theheyway.GPP.AreYouExperienced.AYE;
import me.theheyway.GPP.Economos.Economos;
import me.theheyway.GPP.Overlord.Overlord;
import me.theheyway.GPP.Places.Places;

/**
 * This class is used to check GPP's config files and load the appropriate modules. It also sets the executors to various commands.
 * 
 * @author Way
 *
 */
public class ModuleManager {
	
	public GPP plugin;
	
	//Command Managers
	me.theheyway.GPP.Overlord.Overlord overlord;
	me.theheyway.GPP.AreYouExperienced.AYE aye;
	me.theheyway.GPP.Economos.Economos economos;
	me.theheyway.GPP.Places.Places places;
	
	ModuleManager(GPP plugin) {
		this.plugin = plugin;
		
		//Enable AreYouExperienced
		if (Constants.AREYOUEXPERIENCED_ENABLED) {
			GPP.consoleInfo("[GPP] AreYouExperienced module enabled.");
			aye = new AYE(plugin);
		} else {
			GPP.consoleInfo("[GPP] AreYouExperienced module disabled.");
		}
		
		
		
		//Enable Economos
		if (Constants.ECONOMOS_ENABLED) {
			GPP.consoleInfo("[GPP] Economos module enabled.");
			try {
				economos = new Economos(plugin);
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			//Core Economos commands
			plugin.getCommand("account").setExecutor(economos.commands);
			plugin.getCommand("balance").setExecutor(economos.commands);
			plugin.getCommand("setbalance").setExecutor(economos.commands);
			plugin.getCommand("pay").setExecutor(economos.commands);
			
		} else {
			GPP.consoleInfo("[GPP] Economos module disabled.");
		}
		
		//Enable Places - spawn, homes, warps, teleport commands
		if (Constants.PLACES_ENABLED) {
			GPP.consoleInfo("[GPP] Places module enabled.");
			
			places = new Places(plugin);
			
			plugin.getCommand("home").setExecutor(places.commands);
			plugin.getCommand("jumpto").setExecutor(places.commands);
			plugin.getCommand("put").setExecutor(places.commands);
			plugin.getCommand("spawn").setExecutor(places.commands);
			plugin.getCommand("sethome").setExecutor(places.commands);
			plugin.getCommand("setspawn").setExecutor(places.commands);
			plugin.getCommand("summon").setExecutor(places.commands);
			plugin.getCommand("tp").setExecutor(places.commands);
			
		} else {
			GPP.consoleInfo("[GPP] Places module disabled.");
		}
		
		
		
		//
		overlord = new Overlord(plugin);
		
		//Core Overlord commands
		plugin.getCommand("gpp").setExecutor(overlord.commands);
		plugin.getCommand("cr").setExecutor(overlord.commands);
		plugin.getCommand("point").setExecutor(overlord.commands);
		plugin.getCommand("cm").setExecutor(overlord.commands);
		plugin.getCommand("test").setExecutor(overlord.commands);
		plugin.getCommand("flip").setExecutor(overlord.commands);
		plugin.getCommand("heal").setExecutor(overlord.commands);
		plugin.getCommand("next").setExecutor(overlord.commands);
		plugin.getCommand("prev").setExecutor(overlord.commands);
		plugin.getCommand("page").setExecutor(overlord.commands);
		
		
	}

}
