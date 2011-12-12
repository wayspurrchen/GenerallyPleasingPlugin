package me.theheyway.GPP.Overlord;

import java.util.HashSet;
import java.util.Set;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Util.CommandUtil;
import me.theheyway.GPP.Util.GenUtil;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class General implements CommandExecutor {
	
	private GPP plugin;
	private Overlord overlord;
	public static Set<String> afk = new HashSet<String>();
	
	public General(GPP plugin, Overlord overlord) {
		this.plugin = plugin;
		this.overlord = overlord;
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
		} else if (CommandUtil.cmdEquals(command, "cr")) {
			Chunk curChunk = player.getLocation().getBlock().getChunk();
			int x = curChunk.getX();
			int z = curChunk.getZ();
			for (int i = -3; i < 4; i++ ) { // -3 to 3 for X
				for (int j = -3; j < 4; j++) { // -3 to 3 for Z
					Chunk newChunk = player.getWorld().getChunkAt(x + i, z + j);
					GPP.logger.info(player.getName() + " reloaded chunk (" + (x + i) + "," + (z + i) + ").");
					newChunk.unload(true);
					newChunk.load(true);
				}
			}
			player.sendMessage(ChatColor.YELLOW + "Reloading surrounding chunks.");
			return true;
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
