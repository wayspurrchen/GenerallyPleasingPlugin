package me.theheyway.GPP.Places.Handlers;

import java.sql.SQLException;

import me.theheyway.GPP.Colors;
import me.theheyway.GPP.Exceptions.FrozenException;
import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Exceptions.IncorrectUsageException;
import me.theheyway.GPP.Exceptions.NoPermissionException;
import me.theheyway.GPP.Exceptions.NoPlayerException;
import me.theheyway.GPP.Overlord.Cruelty;
import me.theheyway.GPP.Places.Executors.PlacesExecutor;
import me.theheyway.GPP.Util.Arguments;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TeleportHandler {
	
	public static void direct(Player executor, Arguments args) throws SQLException, GPPException {
		if (args.size()==0) throw new IncorrectUsageException("You must specify player(s) to teleport (towards (one another)).");
		
		if (args.size()==1) {
			if (Cruelty.isFrozen(executor)) throw new FrozenException();
			
			Player target = args.getPlayerMatch(0);
			if (target==null) throw new NoPlayerException();
			
			if (target==executor) {
				executor.sendMessage(Colors.MESSAGE + "You wiggle around a bit.");
			} else {
				PlacesExecutor.teleportPlayerToPlayer(executor, executor, target);
				executor.sendMessage(Colors.MESSAGE + "Teleported to " + Colors.MESSAGE_HIGHLIGHT + target.getName() + Colors.MESSAGE + ".");
			}
			
		} else if (args.size()>=2) {
			//TODO: Add support for teleporting OTHER players to coordinate locations
			if (args.isDouble(0) || args.isDouble(1)) {
				if (args.size()>=3) {
					//If the third arg is a double, then count the second arg as the y value
					if (!args.isDouble(2)) throw new IncorrectUsageException("If teleporting to coordinates, all arguments must be numbers.");
					
					PlacesExecutor.teleportPlayerToCoordinates(executor, args.getDouble(0), args.getDouble(1), args.getDouble(2));
				} else {
					if (args.isDouble(0) && args.isDouble(1)) {
						
						PlacesExecutor.teleportPlayerToCoordinates(executor, args.getDouble(0), -1, args.getDouble(1));
						
					} else throw new IncorrectUsageException("If teleporting to coordinates, all arguments must be numbers.");
				}
				
			} else {
				
				if (!executor.hasPermission("gpp.places.tpother")) throw new NoPermissionException();
			
				Player subject = args.getPlayerMatch(0);
				
				Player target = args.getPlayerMatch(1);
				
				if (subject != null && target != null) {
					if (subject == target) {
						if (subject == executor) {
							executor.sendMessage(Colors.MESSAGE + "Redundancy at its finest.");
						} else {
							executor.sendMessage(Colors.MESSAGE + "You make the person wiggle.");
							subject.sendMessage(Colors.MESSAGE_HIGHLIGHT + executor.getName() + Colors.MESSAGE + " made you wiggle around a bit.");
						}
					} else {
						PlacesExecutor.teleportPlayerToPlayer(executor, subject, target);
						executor.sendMessage(Colors.MESSAGE + "You teleported " + Colors.MESSAGE_HIGHLIGHT + subject.getName() + Colors.MESSAGE + 
								" to " + Colors.MESSAGE_HIGHLIGHT + target.getName() + Colors.MESSAGE + ". ");
					}
				} else throw new IncorrectUsageException("Invalid arguments. Must input player name(s) or coordinate values.");
				
			}
			
			
			
			
	}
		
	}

}
