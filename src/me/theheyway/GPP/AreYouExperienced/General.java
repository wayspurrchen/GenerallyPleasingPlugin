package me.theheyway.GPP.AreYouExperienced;

import me.theheyway.GPP.Util.CommandUtil;
import me.theheyway.GPP.Util.GenUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class General implements CommandExecutor {
	
	private static AYE plugin;
	
	public General(AYE plugin) {
		General.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		Player player = (Player) sender;
		
		//Commands for GPP base
		if (CommandUtil.cmdEquals(command, "gpp")) {
			player.sendMessage(ChatColor.GOLD + plugin.getDescription().getFullName()
					+ ChatColor.WHITE + " made by" + ChatColor.GREEN+ " theheyway" + ChatColor.WHITE + ".");
			return true;
		} else if (CommandUtil.cmdEquals(command, "cm")) {
			if (args.length == 1) {
				if (player.hasPermission("gpp.general.cmother")) {
					Player target = GenUtil.getPlayerMatch(args[0]);
					if (target!=null) {
						
					} else {
						player.sendMessage(ChatColor.RED + "Could not find player.");
						return true;
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permissions to do that.");
					return true;
				}
			} else  {
				
			}
		} else if (CommandUtil.cmdEquals(command, "point")) {
			if (args.length == 1) {
				if (player.hasPermission("gpp.general.pointplayer")) {
					Player target = GenUtil.getPlayerMatch(args[0]);
					if (target!=null) {
						player.setCompassTarget(target.getLocation());
						player.sendMessage(ChatColor.YELLOW + "Compass pointing to " + ChatColor.WHITE + target.getName() + ChatColor.YELLOW + "'s last location.");
						return true;
					} else {
						player.sendMessage(ChatColor.DARK_RED + "Could not find player.");
						return true;
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED + "You don't have permission to do that.");
					return true;
				}
			} else {
				player.setCompassTarget(player.getTargetBlock(null, 70).getLocation());
				player.sendMessage(ChatColor.YELLOW + "Compass pointing to targeted block.");
				return true;
			}
		}
		
		return false;
			
	}
	

}
