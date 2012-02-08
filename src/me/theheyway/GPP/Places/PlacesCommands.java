package me.theheyway.GPP.Places;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Exceptions.FrozenException;
import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Overlord.Cruelty;
import me.theheyway.GPP.Overlord.Overlord;
import me.theheyway.GPP.Places.Executors.PlacesExecutor;
import me.theheyway.GPP.Places.Handlers.HomeHandler;
import me.theheyway.GPP.Places.Handlers.JumpToHandler;
import me.theheyway.GPP.Places.Handlers.PutHandler;
import me.theheyway.GPP.Places.Handlers.SetHomeHandler;
import me.theheyway.GPP.Places.Handlers.SetSpawnHandler;
import me.theheyway.GPP.Places.Handlers.SpawnHandler;
import me.theheyway.GPP.Places.Handlers.SummonHandler;
import me.theheyway.GPP.Places.Handlers.TeleportHandler;
import me.theheyway.GPP.Util.Arguments;
import me.theheyway.GPP.Util.CommandUtil;
import me.theheyway.GPP.Util.SQLUtil;
import me.theheyway.GPP.Util.GenUtil;
import me.theheyway.GPP.Util.MiscUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public class PlacesCommands implements CommandExecutor {
	
	private GPP plugin;
	
	
	public PlacesCommands(GPP plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias,
			String[] arguments) {
		Player executor = (Player) sender;
		Arguments args = new Arguments(arguments);
		
		//Commands for Ports
		try {
			if (CommandUtil.cmdEquals(command, "home")) {
				HomeHandler.direct(executor, args);
			} else if (CommandUtil.cmdEquals(command, "sethome")) {
				SetHomeHandler.direct(executor, args);
			} else if (CommandUtil.cmdEquals(command, "jumpto")) {
				JumpToHandler.direct(executor);
			} else if (CommandUtil.cmdEquals(command, "put")) {
				PutHandler.direct(executor, args);
			} else if (CommandUtil.cmdEquals(command, "setspawn")) {
				//No need for a handler; there's no other checks or anything for this command yet
				PlacesExecutor.setSpawn(executor);
			} else if (CommandUtil.cmdEquals(command, "spawn")) {
				SpawnHandler.direct(executor, args);
			} else if (CommandUtil.cmdEquals(command, "summon")) {
				SummonHandler.direct(executor, args);
			} else if (CommandUtil.cmdEquals(command, "tp")) {
				TeleportHandler.direct(executor, args);
			}
		} catch (GPPException e) {
			executor.sendMessage(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return true;
			
	}
	

}
