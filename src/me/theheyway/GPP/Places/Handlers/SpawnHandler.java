package me.theheyway.GPP.Places.Handlers;

import java.sql.SQLException;

import me.theheyway.GPP.Exceptions.FrozenException;
import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Overlord.Cruelty;
import me.theheyway.GPP.Places.Executors.PlacesExecutor;
import me.theheyway.GPP.Util.Arguments;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class SpawnHandler {
	
	public static void direct(Player executor, Arguments args) throws SQLException, GPPException {
		if (Cruelty.isFrozen(executor)) throw new FrozenException();
		
		World world = null;
		if (args.size()>0) { 
			world = args.getWorldMatch(0);
			if (world==null) throw new GPPException("No such world.");
		} else world = executor.getWorld();
		
		PlacesExecutor.teleportToSpawn(executor, world);
	}

}
