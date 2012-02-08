package me.theheyway.GPP.Places.Handlers;

import java.sql.SQLException;

import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Exceptions.NoPermissionException;
import me.theheyway.GPP.Exceptions.NoPlayerException;
import me.theheyway.GPP.Places.PlaceUtil;
import me.theheyway.GPP.Places.Executors.PlacesExecutor;
import me.theheyway.GPP.Util.Arguments;

import org.bukkit.entity.Player;

public class SetHomeHandler {
	
	public static void direct(Player executor, Arguments args) throws SQLException, GPPException {
		String executorName = executor.getName();
		String executorWorld = executor.getWorld().getName();
		
		//TODO: Add extra argument for specifying worlds. Not the most important thing in the world, admittedly. .. heh. worlds. not the most important thing in the.. nevermind.
		//TODO: Add support for teleporting other players to other homes??
		//TODO: Add support for setting homes of players who don't have homes in the database already
		
		//If we have an argument, run checks.
		if (args.size()==0) {
			//If there's no argument, set the player's own home to the world
			PlacesExecutor.setHome(executor, executorWorld, executorName);
		} else {
			//If there is an argument, check that the player actually has permissions to set other people's homes
			if (!executor.hasPermission("gpp.places.sethomeo")) throw new NoPermissionException("You don't have permission to set other players' homes.");
			
			//If there is an argument, check if the executor is inputting an exact name
			String homeOwner = args.getPlayerNameExact(0);
			if (PlaceUtil.placeExists("home", "home", executorWorld, homeOwner)) {
				PlacesExecutor.setHome(executor, executorWorld, homeOwner);
			} else {
				//If we couldn't find a place for the exact name, try to check for a place for a potentially matched online player
				String matchedHomeOwner = args.getPlayerNameMatch(0);
				if (matchedHomeOwner==null) throw new NoPlayerException();
				PlacesExecutor.setHome(executor, executorWorld, matchedHomeOwner);
			}
		}
		
	}

}
