package me.theheyway.GPP;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public abstract class Stage {

	public static Map<Player, Stage> playerStages = new HashMap<Player, Stage>();
	
	Player player;
	
	public Stage(Player player) {
		this.player = player;
		playerStages.put(player, this);
	}
 	
}
