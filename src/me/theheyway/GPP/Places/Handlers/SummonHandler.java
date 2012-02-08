package me.theheyway.GPP.Places.Handlers;

import java.sql.SQLException;

import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Exceptions.IncorrectUsageException;
import me.theheyway.GPP.Exceptions.NoPlayerException;
import me.theheyway.GPP.Places.Executors.PlacesExecutor;
import me.theheyway.GPP.Util.Arguments;
import me.theheyway.GPP.Util.GenUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SummonHandler {
	
	public static void direct(Player executor, Arguments args) throws SQLException, GPPException {
		if (args.size() >= 1) {
			if (args.getString(0)!="all") {
				Player teleporter = GenUtil.getPlayerMatch(args.getString(0));
				if (teleporter==null) throw new NoPlayerException();
				PlacesExecutor.teleportPlayerToPlayer(executor, teleporter, executor);
				executor.sendMessage(ChatColor.YELLOW + "Teleported " + ChatColor.WHITE + teleporter.getName() + ChatColor.YELLOW + " to you.");
				teleporter.sendMessage(ChatColor.WHITE + executor.getName() + ChatColor.YELLOW + " has summoned you.");
			} else {
				Player[] all = Bukkit.getServer().getOnlinePlayers();
				for (int i = 0; i < all.length; i++) {
					if (all[i]!=executor) {
						PlacesExecutor.teleportPlayerToPlayer(executor, all[i], executor);
						all[i].sendMessage(ChatColor.WHITE + executor.getName() + ChatColor.YELLOW + " has summoned you.");
					}
				}
			}
		} else throw new IncorrectUsageException("You must specify a player to summon.");
	}

}
