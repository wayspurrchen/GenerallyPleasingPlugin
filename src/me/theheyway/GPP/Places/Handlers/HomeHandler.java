package me.theheyway.GPP.Places.Handlers;

import java.sql.SQLException;

import me.theheyway.GPP.Exceptions.FrozenException;
import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Overlord.Cruelty;
import me.theheyway.GPP.Places.PlaceUtil;
import me.theheyway.GPP.Util.Arguments;
import me.theheyway.GPP.Util.GenUtil;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeHandler {
	
	public static boolean direct(Player executor, Arguments args) throws NumberFormatException, SQLException, GPPException {
		String executorName = executor.getName();
		
		if (Cruelty.isFrozen(executor)) throw new FrozenException();
		
		//TODO: Add extra argument for specifying worlds.
		//If we have an argument, run checks.
		if (args.size()==1) {
			//If the passed name exactly matches a player, online or offline, send to executor
			if (PlaceUtil.placeExists("home", "home", executor.getWorld().getName(), args.getString(1))) {
				
			} else {
				String target = GenUtil.getPlayerMatchString(args.getString(0));
				if (PlaceUtil.placeExists("home", "home", executor.getWorld().getName(), target)) {
					PlacesExecutor.
				}
			}
			
			String target = GenUtil.getPlayerMatchString(args.getString(0));
			if (PlaceUtil.placeExists("home", "home", target, executor.getWorld().getName())) {
				double coords[] = PlaceUtil.getPlace("home", "home", target, executor.getWorld().getName());
				Location loc = new Location(executor.getWorld(), coords[0], coords[1], coords[2]);
				loc.setYaw((float) coords[3]);
				executor.teleport(loc);
				executor.sendMessage(ChatColor.YELLOW + "Teleported to " + ChatColor.WHITE + target + ChatColor.YELLOW
						+ "'s home.");
				return true;
			} else {
				executor.sendMessage(ChatColor.DARK_RED + "Player " + ChatColor.RED + args[0] + ChatColor.DARK_RED + " does not have a home.");
				return true;
			}
			
		//If no arguments, run check to see if our player has a home in this world. If they don't, throw an error.
		} else if (!PlaceUtil.placeExists("home", "home", executor.getName(), executor.getWorld().getName())) throw new NoHomeException();
		
		double coords[] = PlaceUtil.getPlace("home", "home", executor.getName(), executor.getWorld().getName());
		
		Location loc = new Location(executor.getWorld(), coords[0], coords[1], coords[2]);
		loc.setYaw((float) coords[3]);
		executor.teleport(loc);
		return true;
		
	}

}
