package me.theheyway.GPP.Places.Handlers;

import java.sql.SQLException;

import me.theheyway.GPP.Exceptions.FrozenException;
import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Overlord.Cruelty;
import me.theheyway.GPP.Places.Executors.PlacesExecutor;

import org.bukkit.entity.Player;

public class JumpToHandler {
	
	public static void direct(Player executor) throws SQLException, GPPException {
		
		//Is the player frozen?
		if (Cruelty.isFrozen(executor)) throw new FrozenException();
		
		PlacesExecutor.teleportPlayerToTargetBlock(executor, executor);
		
	}

}
