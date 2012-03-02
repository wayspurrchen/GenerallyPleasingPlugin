package me.theheyway.GPP.Places.Handlers;

import java.sql.SQLException;

import me.theheyway.GPP.Colors;
import me.theheyway.GPP.Exceptions.FrozenException;
import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Exceptions.IncorrectUsageException;
import me.theheyway.GPP.Exceptions.InvalidArgumentException;
import me.theheyway.GPP.Exceptions.NoPermissionException;
import me.theheyway.GPP.Exceptions.NoPlayerException;
import me.theheyway.GPP.Overlord.Cruelty;
import me.theheyway.GPP.Places.PlaceUtil;
import me.theheyway.GPP.Places.Executors.PlacesExecutor;
import me.theheyway.GPP.Places.Executors.WarpExecutor;
import me.theheyway.GPP.Util.Arguments;
import me.theheyway.GPP.Util.StringUtil;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WarpHandler {
	
	public static void direct(Player executor, Arguments args) throws SQLException, GPPException {
		if (args.size()==0) throw new IncorrectUsageException("You must specify player(s) to teleport (towards (one another)).");
		
		if (args.size()>=1) {
			if (args.getString(1).equals("list")) {
				
			}
			
		} else if (args.size()==2) {
			if (args.getString(0).equals("create") || args.getString(0).equals("make") || args.getString(0).equals("new")) {
				String warpName = args.getString(1);
				if (!StringUtil.isCleanString(warpName)) throw new InvalidArgumentException("Warp name must be alphanumeric and have no spaces.");
				
				if (PlaceUtil.placeExists(warpName, "warp", executor.getWorld().getName(), executor.getName())) throw new GPPException("Warp already exists.");
				
				WarpExecutor.createWarp(executor, warpName);
			}
		}
		
	}

}
