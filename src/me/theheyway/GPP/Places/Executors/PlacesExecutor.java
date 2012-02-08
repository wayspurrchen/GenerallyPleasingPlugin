package me.theheyway.GPP.Places.Executors;

import java.sql.SQLException;

import me.theheyway.GPP.Colors;
import me.theheyway.GPP.Executor;
import me.theheyway.GPP.Places.Place;
import me.theheyway.GPP.Places.PlaceUtil;
import me.theheyway.GPP.Places.Places;
import me.theheyway.GPP.Util.BlockUtil;
import me.theheyway.GPP.Util.ChunkUtil;
import me.theheyway.GPP.Util.PlayerUtil;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


public class PlacesExecutor extends Executor {
	
	public static void teleportPlayerToTargetBlock(Player executor, Player teleporter) {
		Block surfaceBlock = BlockUtil.getSurfaceBlock(executor.getTargetBlock(null, 100));
		if (surfaceBlock==null) executor.sendMessage(Colors.ERROR + "No space to jump to.");
		else {
			PlayerUtil.teleportPlayerToBlock(teleporter, surfaceBlock);
			//If we're putting someone somewhere else, send a message; otherwise, don't bother the executor with messages on a simple command like /jumpto
			if (!executor.equals(teleporter)) executor.sendMessage(Colors.MESSAGE_HIGHLIGHT + teleporter.getName() + Colors.MESSAGE + " teleported to target block.");
		}
	}
	
	public static void teleportPlayerHome(Player executor, Player teleporter, Place home) {
		PlaceUtil.teleportPlayerToPlace(teleporter, home);
		ChunkUtil.loadChunkAtLocation(home.generateLocation(), true);
		if (home.getOwner().equals(executor.getName())) {
		executor.sendMessage(Colors.MESSAGE + "You have teleported home.");
		} else {
			//Is the player being sent to their own home?
			if (home.getOwner().equalsIgnoreCase(teleporter.getName()))	executor.sendMessage(Colors.MESSAGE + "You have sent " +
					Colors.MESSAGE_HIGHLIGHT + teleporter.getName() + Colors.MESSAGE + " to their home.");
			//If not, tell the executor that the player they teleported went to the right home.
			else executor.sendMessage(Colors.MESSAGE + "You have sent " +
					Colors.MESSAGE_HIGHLIGHT + teleporter.getName() + Colors.MESSAGE + " to " +
					Colors.MESSAGE_HIGHLIGHT + home.getOwner() + Colors.MESSAGE + "'s home.");
		}
	}
	
	public static void teleportPlayerToPlayer(Player executor, Player teleporter, Player teleportee) {
		ChunkUtil.loadChunkAtLocation(teleportee.getLocation(), true);
		teleporter.teleport(teleportee);
	}
	
	public static void teleportPlayerToCoordinates(Player executor, double x, double y, double z) {
		Location loc = new Location(executor.getWorld(), x, y, z);
		if (y == -1) {
			loc.setY(BlockUtil.getSurfaceBlockFromTopOfWorld(executor.getWorld(), x, z).getY()+1);
			y = loc.getY();
		}
		ChunkUtil.loadChunkAtLocation(loc, true);
		executor.teleport(loc);
		executor.sendMessage(Colors.MESSAGE + "You have teleported to (" + Colors.MESSAGE_HIGHLIGHT + x + Colors.MESSAGE + ", " +
				Colors.MESSAGE_HIGHLIGHT + y + Colors.MESSAGE + ", " + Colors.MESSAGE_HIGHLIGHT + z + Colors.MESSAGE + ").");
	}
	
	public static void setHome(Player executor, String world, String playerName) throws SQLException {
		//If the place of the player whose home is being set already exists, update the XYZ to the new location based on the executor's position
		double x = executor.getLocation().getX();
		double y = executor.getLocation().getY();
		double z = executor.getLocation().getZ();
		double yaw = executor.getLocation().getYaw();
		if (PlaceUtil.placeExists("home","home",world,playerName)) {
			PlaceUtil.updateXYZ1("home", "home", world, playerName, x, y, z);
			PlaceUtil.updateYaw("home", "home", world, playerName, yaw);
			if (executor.getName().equalsIgnoreCase(playerName)) {
				executor.sendMessage(Colors.MESSAGE + "Updated home in " + Colors.MESSAGE_HIGHLIGHT + world + Colors.MESSAGE + ".");
			} else {
				executor.sendMessage(Colors.MESSAGE + "Updated " + Colors.MESSAGE_HIGHLIGHT + playerName + Colors.MESSAGE + "'s home in " + Colors.MESSAGE_HIGHLIGHT + world + Colors.MESSAGE + ".");
			}
		} else {
			Place home = new Place(executor);
			home.setOwner(playerName);
			home.setName("home");
			home.setType("home");
			home.insert();
			if (executor.getName().equalsIgnoreCase(playerName)) {
				executor.sendMessage(Colors.MESSAGE + "Set home in " + Colors.MESSAGE_HIGHLIGHT + world + Colors.MESSAGE + ".");
			} else {
				executor.sendMessage(Colors.MESSAGE + "Set " + Colors.MESSAGE_HIGHLIGHT + playerName + Colors.MESSAGE + "'s home in " + Colors.MESSAGE_HIGHLIGHT + world + Colors.MESSAGE + ".");
			}
		}
	}
	
	public static void setSpawn(Player executor) {
		Location loc = executor.getLocation();
		int x = (int) loc.getX();
		int y = (int) loc.getY();
		int z = (int) loc.getZ();
		double yaw = loc.getYaw();
		executor.getWorld().setSpawnLocation(x, y, z);
		FileConfiguration spawnYaws = Places.getSpawnYaws();
		spawnYaws.set(executor.getWorld().getName(), yaw);
		Places.saveSpawnYaws();
		executor.sendMessage(ChatColor.YELLOW + "Spawn for " + ChatColor.WHITE + executor.getWorld().getName()
				+ ChatColor.YELLOW + " updated.");
	}
	
	public static void teleportToSpawn(Player executor, World world) {
		FileConfiguration spawnYaws = Places.getSpawnYaws();
		Location loc = world.getSpawnLocation();
		ChunkUtil.loadChunkAtLocation(loc, true);
		loc.setYaw((float) spawnYaws.getDouble(executor.getWorld().getName(), 0.0));
		executor.teleport(loc);
	}
	

}
