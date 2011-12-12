package me.theheyway.GPP.Overlord;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Util.CommandUtil;
import me.theheyway.GPP.Util.GenUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Cruelty implements CommandExecutor {
	
	public static Set<String> frozen = new HashSet<String>();
	
	private GPP plugin;

	private Overlord overlord;
	
	public Cruelty(GPP plugin, Overlord overlord) {
		this.plugin = plugin;
		this.overlord = overlord;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		Player player = (Player) sender;
		
		//Commands for GPP base
		if (CommandUtil.cmdEquals(command, "freeze") && args.length == 1) {
			Player target = GenUtil.getPlayerMatch(args[0]);
			if (target!=null) {
				String targetName = target.getName();
				if (!isFrozen(target)) {
					freeze(target);
					player.sendMessage(ChatColor.WHITE + targetName + ChatColor.LIGHT_PURPLE + " has been frozen.");
					return true;
				} else {
					unfreeze(target);
					player.sendMessage(ChatColor.WHITE + targetName + ChatColor.LIGHT_PURPLE + " has been unfrozen.");
					return true;
				}
			} else {
				player.sendMessage(ChatColor.RED + "Could not find player.");
				return true;
			}
		}
		
		return false;
			
	}
	
	public static void freeze(Player player) {
		frozen.add(player.getName());
	}
	
	public static void unfreeze(Player player) {
		frozen.remove(player.getName());
	}
	
    public static boolean isFrozen(Player player) {
    	String playerName = player.getName();
    	if (Cruelty.frozen.contains(playerName)) {
			//GPP.logger.info(playerName + "is frozen");
    		return true;
    	} else {
			//GPP.logger.info(playerName + " not frozen");
    		return false;
    	}
    }

}
