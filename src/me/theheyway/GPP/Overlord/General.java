package me.theheyway.GPP.Overlord;

import java.util.HashSet;
import java.util.Set;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Util.CommandUtil;
import me.theheyway.GPP.Util.GenUtil;
import me.theheyway.GPP.Util.PlayerUtil;
import me.theheyway.GPP.Util.TypeUtil;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
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

	/***
	 * onCommand Intercept user commands
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		Player player = (Player) sender;

		// Commands for GPP base
		if (CommandUtil.cmdEquals(command, "gpp")) {
			player.sendMessage(ChatColor.GOLD
					+ plugin.getDescription().getFullName() + ChatColor.WHITE
					+ " made by" + ChatColor.GREEN + " theheyway"
					+ ChatColor.WHITE + ".");
			return true;
		} else if (CommandUtil.cmdEquals(command, "cm")) { // Creative mode Toggle
			if (args.length == 1) {
				if (player.hasPermission("gpp.general.cmother")) {
					Player target = GenUtil.getPlayerMatch(args[0]);
					if (target != null) {
						if (target.getGameMode().compareTo(GameMode.CREATIVE) == 0) {
							target.setGameMode(GameMode.SURVIVAL);
							target.sendMessage(ChatColor.WHITE
									+ player.getName() + ChatColor.YELLOW + " has set your gamemode to "
									+ ChatColor.WHITE + "survival" + ChatColor.YELLOW + ".");
						} else {
							target.setGameMode(GameMode.CREATIVE);
							target.sendMessage(ChatColor.WHITE
									+ player.getName() + ChatColor.YELLOW + " has set your gamemode to "
									+ ChatColor.WHITE + "creative" + ChatColor.YELLOW + ".");
						}
						return true;
					} else {
						player.sendMessage(ChatColor.DARK_RED
								+ "Could not find player.");
						return true;
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED
							+ "You do not have permission to do that.");
					return true;
				}
			} else { //Toggle own creative mode
				if (player.getGameMode().compareTo(GameMode.CREATIVE) == 0) {
					player.setGameMode(GameMode.SURVIVAL);
					player.sendMessage(ChatColor.YELLOW + "Toggled gamemode to "
						+ ChatColor.WHITE + "survival" + ChatColor.YELLOW + ".");
				} else {
					player.setGameMode(GameMode.CREATIVE);
					player.sendMessage(ChatColor.YELLOW + "Toggled gamemode to "
							+ ChatColor.WHITE + "creative" + ChatColor.YELLOW + ".");
				}
				return true;
			}
		} else if (CommandUtil.cmdEquals(command, "cr")) { // Chunk Reload TODO:FIX
			Chunk curChunk = player.getLocation().getBlock().getChunk();
			int x = curChunk.getX();
			int z = curChunk.getZ();
			for (int i = -3; i < 4; i++) { // -3 to 3 for X
				for (int j = -3; j < 4; j++) { // -3 to 3 for Z
					Chunk newChunk = player.getWorld().getChunkAt(x + i, z + j);
					GPP.logger.info(player.getName() + " reloaded chunk ("
							+ (x + i) + "," + (z + i) + ").");
					newChunk.unload(true);
					newChunk.load(true);
				}
			}
			player.sendMessage(ChatColor.YELLOW
					+ "Reloading surrounding chunks.");
			return true;
		} else if (CommandUtil.cmdEquals(command, "point")) { // Change compass point location to current viewed block
			if (args.length == 1) {
				if (player.hasPermission("gpp.general.pointplayer")) {
					Player target = GenUtil.getPlayerMatch(args[0]);
					if (target != null) {
						player.setCompassTarget(target.getLocation());
						player.sendMessage(ChatColor.YELLOW
								+ "Compass pointing to " + ChatColor.WHITE
								+ target.getName() + ChatColor.YELLOW
								+ "'s last location.");
						return true;
					} else {
						player.sendMessage(ChatColor.DARK_RED
								+ "Could not find player.");
						return true;
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED
							+ "You don't have permission to do that.");
					return true;
				}
			} else {
				player.setCompassTarget(player.getTargetBlock(null, 70)
						.getLocation());
				player.sendMessage(ChatColor.YELLOW
						+ "Compass pointing to targeted block.");
				return true;
			}
		} else if(CommandUtil.cmdEquals(command, "flipcoin")) { //coin flipping
			
			//result finding
			double result = Math.random();
			GPP.logger.info("coin flip : " + result);
			String message = "";
			if(result <= 0.5){
				message = ChatColor.YELLOW
						+ player.getName() + " flipped a coin. Result is Heads.";
			} else {
				message = ChatColor.YELLOW
						+ player.getName() + " flipped a coin. Result is Tails.";
			}
			
			//message dispatch
			if (args.length == 0) {
					GPP.server.broadcastMessage(message);
			} else if (args.length == 1) {
				Player target = GenUtil.getPlayerMatch(args[0]);
				if (target != null) {
					player.sendMessage(message);
					target.sendMessage(message);
				} else {
					if(args[0].equals("help")) //Display help
						return false;
					
					player.sendMessage(ChatColor.DARK_RED
							+ "Could not find player.");
				}
			}
			
			return true;
			
		} else if (CommandUtil.cmdEquals(command, "heal")) {
				if(args.length == 2){
					Player target = GenUtil.getPlayerMatch(args[0]);
					
					if(target != null) {
						if (TypeUtil.isInteger(args[1])) { // integer check
							int amount = Integer.parseInt(args[1]);
							PlayerUtil.heal(target, amount);
						} else {
							player.sendMessage(ChatColor.DARK_RED + "Must enter an integer value.");
						}
					} else {
						player.sendMessage(ChatColor.DARK_RED + "Could not find player.");
					}
						
				} else if (args.length == 1) {
					if(args[0].equals("help")) //Display help
							return false;
					
					else {
						Player target = GenUtil.getPlayerMatch(args[0]);
						if (target!=null) {
							int amount = Integer.parseInt(args[1]);
							PlayerUtil.heal(target, amount);
						} else {
							player.sendMessage(ChatColor.DARK_RED + "Could not find player.");
							return true;
						}
					}
				} else {
					PlayerUtil.heal(player, 20);
				}
			}

		return false;

	}

}
