package me.theheyway.GPP.AreYouExperienced;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;

import me.theheyway.GPP.GPP;

import static me.theheyway.GPP.AreYouExperienced.AYEConstants.*;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;

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
	AYEConstants constants;
	
	private GPP plugin;
	
	static File aye_config_file = new File(AYE_CONFIG_PATH);
	static FileConfiguration aye_config = null;
	static HashMap<Player, Double> expAccumulator = new HashMap<Player, Double>();
	
	public AYE(GPP plugin) {
		this.plugin = plugin;
		
		//Set up module config
		reloadConfig();
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		constants = new AYEConstants(plugin, this);
		
	}
	
	public static void addAccumulatedExp(Player player, double value) {
		value *= AYEConstants.EXP_MULTIPLIER;
		double oldVal = 0;
		if (expAccumulator.get(player)!=null) oldVal = expAccumulator.get(player);
		setAccumulatedExp(player, oldVal + value);
	}
	
	private static void setAccumulatedExp(Player player, double value) {
		expAccumulator.put(player, value);
		//GPP.consoleInfo("Put "+ value+ " into player's expAccumulator slot");
		if (expAccumulator.get(player) > 1) {
			//GPP.consoleInfo("Player expAccumulator found to be more than 1: "+expAccumulator.get(player));
			distributeExp(player);
		}
	}
	
	static void distributeExp(Player player) {
		double playerExp = expAccumulator.get(player);
		//GPP.consoleInfo("Distributing process| CURRENT PLAYER EXP: " + playerExp);
		double value = Math.floor(playerExp);
		//GPP.consoleInfo("Distributing process| FLOORED VALUE: " + value);
		expAccumulator.put(player, playerExp - value);
		//GPP.consoleInfo("Distributing process| NEW EXPACCUMULATOR VALUE AFTER REMOVING FLOORED FROM CURRENT PLAYER EXP: " + expAccumulator.get(player));
		ExperienceOrb orb = player.getWorld().spawn(player.getLocation(), ExperienceOrb.class);
		orb.setExperience((int) value);
		//GPP.logger.info("Spawned orb on " + player.getName() + ", exp: "+orb.getExperience());
		orb.teleport(player);
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
	
	public static void stillAliveReward() {
		Player[] players = GPP.server.getOnlinePlayers();
		for (int i = 0; i < players.length; i++) {
			players[i].sendMessage(ChatColor.YELLOW + "You have survived through the night and gained experience.");
			addAccumulatedExp(players[i], AYEConstants.STILL_ALIVE_EXP);
		}
	}
		
}