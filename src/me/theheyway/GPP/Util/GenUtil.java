package me.theheyway.GPP.Util;

import java.util.List;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;

import me.theheyway.GPP.GPP;

public class GenUtil {
	
	public static Player getPlayerMatch(String playersubstring) {
		List<Player> playerList = GPP.server.matchPlayer(playersubstring);
		if (playerList.size() > 0) return playerList.get(0);
		else return null;
	}
	
	public static String getPlayerMatchString(String playersubstring) {
		List<Player> playerList = GPP.server.matchPlayer(playersubstring);
		if (playerList.size() > 0) return playerList.get(0).getName();
		else return null;
	}
	
	public static void updateExp(Player player) {
		player.setLevel(player.getLevel());
		ExperienceOrb orb = player.getWorld().spawn(player.getLocation(), ExperienceOrb.class);
		orb.setExperience(1);
		GPP.logger.info("Spawned orb, exp: "+orb.getExperience());
		orb.teleport(player);
	}
	
}
