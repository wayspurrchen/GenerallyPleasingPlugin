package me.theheyway.GPP.Places.Handlers;

import java.sql.SQLException;

import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Exceptions.NoPlayerException;
import me.theheyway.GPP.Places.Executors.PlacesExecutor;
import me.theheyway.GPP.Util.Arguments;

import org.bukkit.entity.Player;

public class PutHandler {
	
	public static void direct(Player executor, Arguments args) throws SQLException, GPPException {
		
		if (args.size()<1) throw new GPPException("Must provide a player to put to target block.");
		
		Player target = args.getPlayerExact(0);
		if (target!=null) {
			PlacesExecutor.teleportPlayerToTargetBlock(executor, target);
		} else {
			Player targetMatched = args.getPlayerMatch(0);
			if (targetMatched==null) throw new NoPlayerException();
			PlacesExecutor.teleportPlayerToTargetBlock(executor, targetMatched);
		}
		
		
	}

}
