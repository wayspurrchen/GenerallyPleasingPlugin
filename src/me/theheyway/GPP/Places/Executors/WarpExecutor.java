package me.theheyway.GPP.Places.Executors;

import java.sql.SQLException;

import me.theheyway.GPP.Executor;
import me.theheyway.GPP.Places.Place;
import me.theheyway.GPP.Places.PlaceUtil;

import org.bukkit.entity.Player;


public class WarpExecutor extends Executor {
	
	public static void createWarp(Player executor, String name) throws SQLException {
		//Create new place based off player's information, set the type to warp, and set the name to the name passed
		Place place = new Place(executor);
		place.setType("warp");
		place.setName(name);
		
		//Put 'er in!
		PlaceUtil.insertPlace(place);
	}
	
	public static void listWarps() {
		
	}
	

}
