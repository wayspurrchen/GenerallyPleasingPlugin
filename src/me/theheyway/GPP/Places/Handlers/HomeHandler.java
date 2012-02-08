package me.theheyway.GPP.Places.Handlers;

import java.sql.SQLException;

import me.theheyway.GPP.Colors;
import me.theheyway.GPP.Exceptions.FrozenException;
import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Exceptions.NoHomeException;
import me.theheyway.GPP.Exceptions.NoPermissionException;
import me.theheyway.GPP.Overlord.Cruelty;
import me.theheyway.GPP.Places.Place;
import me.theheyway.GPP.Places.PlaceUtil;
import me.theheyway.GPP.Places.Executors.PlacesExecutor;
import me.theheyway.GPP.Util.Arguments;
import me.theheyway.GPP.Util.GenUtil;

import org.bukkit.entity.Player;

public class HomeHandler {
	
	public static boolean direct(Player executor, Arguments args) throws SQLException, GPPException {
		String executorName = executor.getName();
		String executorWorld = executor.getWorld().getName();
		
		if (Cruelty.isFrozen(executor)) throw new FrozenException(); //TODO: add checks for whether they're trying to teleport themselves or someone else. but probably not worth the effort at all
		
		//TODO: Add extra argument for specifying worlds. Not the most important thing in the world, admittedly. .. heh. worlds. not the most important thing in the.. nevermind.
		//TODO: Add support for teleporting other players to other homes??
		
		//If we have an argument, run checks.
		if (args.size()==1) {
			
			//Check that the player has permissions to go to other people's homes
			if (!executor.hasPermission("gpp.places.homeo")) throw new NoPermissionException();
			
			String homeOwner = args.getString(0);
			
			//If the passed name exactly matches a player, online or offline, send to executor
			if (PlaceUtil.placeExists("home", "home", executorWorld, homeOwner)) {
				//Since it matched an online player, get the home data from the DB and create 
				Place home = PlaceUtil.getMinimalPlace("home","home",executorWorld,homeOwner);
				PlacesExecutor.teleportPlayerHome(executor, executor, home);
			//If not, try to match it to a player.
			} else {
				String matchedHomeOwner = GenUtil.getPlayerMatchString(args.getString(0));
				if (PlaceUtil.placeExists("home", "home", executorWorld, matchedHomeOwner)) {
					Place home = PlaceUtil.getMinimalPlace("home","home",executorWorld,matchedHomeOwner);
					PlacesExecutor.teleportPlayerHome(executor, executor, home);
				} else throw new NoHomeException("Player " + Colors.ERROR_HIGHLIGHT + homeOwner + Colors.ERROR + " does not have a home in this world.");
			}
			
		//If no arguments, run check to see if our player has a home in this world. If they don't, throw an error.
		} else {
			if (!PlaceUtil.placeExists("home", "home", executorWorld, executorName)) throw new NoHomeException();
		
			Place home = PlaceUtil.getPlace("home", "home", executorWorld, executorName);
			PlacesExecutor.teleportPlayerHome(executor, executor, home);
		}
		
		return true;
		
	}

}
