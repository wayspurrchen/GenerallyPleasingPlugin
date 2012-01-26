package me.theheyway.GPP.Places;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Exceptions.FrozenException;
import me.theheyway.GPP.Overlord.Cruelty;
import me.theheyway.GPP.Overlord.Overlord;
import me.theheyway.GPP.Places.Handlers.HomeHandler;
import me.theheyway.GPP.Util.Arguments;
import me.theheyway.GPP.Util.CommandUtil;
import me.theheyway.GPP.Util.SQLUtil;
import me.theheyway.GPP.Util.GenUtil;
import me.theheyway.GPP.Util.MiscUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public class PlacesCommands implements CommandExecutor {
	
	private GPP plugin;
	
	
	public PlacesCommands(GPP plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias,
			String[] arguments) {
		Player executor = (Player) sender;
		Arguments args = new Arguments(arguments);
		
		//Commands for Ports
		if (CommandUtil.cmdEquals(command, "home")) {
			HomeHandler.direct(executor, args);
		} else if (CommandUtil.cmdEquals(command, "sethome")) {
			try {
				if (args.size()==0) {
					if (setHome(executor, executor.getName())) return true;
				} else {
					if (PlaceUtil.placeExists("home", "home", args[0], executor.getWorld().getName())) {
						if (setHome(executor, args[0])) return true;
					} else {
						Player subject = GenUtil.getPlayerMatch(args[0]);
						if (subject!=null) {
							setHome(executor, subject.getName());
							return true;
						} else {
							executor.sendMessage(ChatColor.DARK_RED + "Could not find player.");
							return true;
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (CommandUtil.cmdEquals(command, "jumpto")) {
			if (!Cruelty.isFrozen(executor)) {
				teleportToBlock(executor, executor);
				return true;
			} else {
				executor.sendMessage(ChatColor.RED + "You have not set a home on this world. Use /sethome to do so.");
				return true;
			}
		} else if (CommandUtil.cmdEquals(command, "put")) {
			if (args.length==1) {
				Player target = GenUtil.getPlayerMatch(args[0]);
				if (target!=null) {
					teleportToBlock(executor, target);
					executor.sendMessage(ChatColor.YELLOW + "Put " + ChatColor.WHITE + target.getName() + ChatColor.YELLOW + " to target.");
					return true;
				} else {
					executor.sendMessage(ChatColor.DARK_RED + "Could not find player.");
					return true;
				}
			} else {
				executor.sendMessage(ChatColor.DARK_RED + "Could not find player.");
				return true;
			}
		
		} else if (CommandUtil.cmdEquals(command, "setspawn")) { //Set save yaw
			Location loc = executor.getLocation();
			int x = (int) loc.getX();
			int y = (int) loc.getY();
			int z = (int) loc.getZ();
			double yaw = loc.getYaw();
			executor.getWorld().setSpawnLocation(x, y, z);
			FileConfiguration spawnYaws = getSpawnYaws();
			spawnYaws.set(executor.getWorld().getName(), yaw);
			saveSpawnYaws();
			executor.sendMessage(ChatColor.YELLOW + "Spawn for " + ChatColor.WHITE + executor.getWorld().getName()
					+ ChatColor.YELLOW + " updated.");
			return true;
		} else if (CommandUtil.cmdEquals(command, "spawn")) {
			if (!Cruelty.isFrozen(executor)) {
				FileConfiguration spawnYaws = getSpawnYaws();
				Location loc = executor.getWorld().getSpawnLocation();
				loc.setYaw((float) spawnYaws.getDouble(executor.getWorld().getName(), 0.0));
				executor.teleport(loc);
				return true;
			} else {
				executor.sendMessage(ChatColor.RED + "You are frozen.");
			}
		} else if (CommandUtil.cmdEquals(command, "summon")) {
			if (args.length > 1) {
				if (args[0]!="all") {
					Player target = GenUtil.getPlayerMatch(args[0]);
					if (target!=null) {
						target.teleport(executor);
						executor.sendMessage(ChatColor.YELLOW + "Teleported " + ChatColor.WHITE + target.getName() + ChatColor.YELLOW + " to you.");
						target.sendMessage(ChatColor.WHITE + executor.getName() + ChatColor.YELLOW + " has summoned you.");
					} else {
						executor.sendMessage(ChatColor.DARK_RED + "Could not find player.");
						return true;
					}
				} else {
					Player[] all = Bukkit.getServer().getOnlinePlayers();
					for (int i = 0; i < all.length; i++) {
						if (all[i]!=executor) {
							all[i].teleport(executor);
							all[i].sendMessage(ChatColor.WHITE + executor.getName() + ChatColor.YELLOW + " has summoned you.");
						}
					}
				}
			} else return false;
		} else if (CommandUtil.cmdEquals(command, "tp")) {
			if (!Cruelty.isFrozen(executor)) {
				if (args.length>0) {
					if (args.length==1) {
						Player target = GenUtil.getPlayerMatch(args[0]);
						if (target!=null) {
							if (target==executor) {
								executor.sendMessage(ChatColor.YELLOW + "You wiggle around a bit.");
								return true;
							} else {
								executor.teleport(target);
								executor.sendMessage(ChatColor.YELLOW + "Teleported to " + ChatColor.WHITE + target.getName() + ChatColor.YELLOW + ".");
								return true;
							}
						} else {
							executor.sendMessage(ChatColor.DARK_RED + "Could not find player.");
							return true;
						}
					} else if (args.length>=2) {
						if (executor.hasPermission("gpp.ports.tpother")) {
							Player target1 = GenUtil.getPlayerMatch(args[0]);
							Player target2 = GenUtil.getPlayerMatch(args[1]);
							if (target1 != null && target2 != null) {
								if (target1 == target2) {
									if (target1 == executor) {
										executor.sendMessage(ChatColor.YELLOW + "Redundancy at its finest.");
										return true;
									} else {
										executor.sendMessage(ChatColor.YELLOW + "You make the person wiggle.");
										target1.sendMessage("You wiggle around a bit.");
										return true;
									}
								} else {
									target1.teleport(target2);
									executor.sendMessage(ChatColor.YELLOW + "You teleported " + ChatColor.WHITE + target1.getName() + ChatColor.YELLOW + 
											" to " + ChatColor.WHITE + target2.getName() + ChatColor.YELLOW + ". ");
									return true;
								}
							} else {
								executor.sendMessage(ChatColor.DARK_RED + "Could not find one or both players.");
								return true;
							}
						}
					}
				} else return false;
			} else {
				executor.sendMessage(ChatColor.RED + "You are frozen.");
			}
		}
		
		return false;
			
	}
	
	private void teleportToBlock(Player player, Player subject) {
		Block targetBlock = player.getTargetBlock(null, 70);
		Block aboveBlock = targetBlock.getRelative(BlockFace.UP, 1);
		while (aboveBlock.getType()!=Material.AIR) {
			aboveBlock = aboveBlock.getRelative(BlockFace.UP, 1);
		}
		targetBlock = aboveBlock;
		Location targetLoc = targetBlock.getLocation();
		targetLoc.add(.5, 0, .5); // So we're on top of, and in the center of the block
		targetLoc.setYaw(player.getLocation().getYaw());
		targetLoc.setPitch(player.getLocation().getPitch());
		subject.teleport(targetLoc);
	}
	
	private boolean setHome(Player caller, String playerName) throws SQLException {
		Location loc = caller.getLocation();
		int x = (int) loc.getX();
		int y = (int) loc.getY();
		int z = (int) loc.getZ();
		double yaw = loc.getYaw();
		if (PlaceUtil.placeExists("home", "home", playerName, caller.getWorld().getName())) {
			String update = "UPDATE " + DB_LOCATIONS_TABLENAME + " SET x=" + x +
					", y=" + y + ", z=" + z + ", yaw=" + yaw +
					" WHERE type='home' AND name='home' AND owner='" + playerName +
					"' AND world='" + caller.getWorld().getName() + "'";
			SQLUtil.transactUpdate(update);
			if (caller.getName()!=playerName) {
				caller.sendMessage(ChatColor.YELLOW + "Home location in " + ChatColor.WHITE + caller.getWorld().getName() +
						ChatColor.YELLOW + " for " + ChatColor.WHITE + playerName + ChatColor.YELLOW + " updated.");
			} else caller.sendMessage(ChatColor.YELLOW + "Home location in " + ChatColor.WHITE + caller.getWorld().getName() +
					ChatColor.YELLOW + " updated.");
			return true;
		} else {
			String update = "INSERT INTO " + DB_LOCATIONS_TABLENAME + 
					" (type, name, owner, world, x, y, z, yaw) VALUES ('home', 'home', '" + playerName
					+ "', '" + caller.getWorld().getName() + "', " + x + ", " + y + ", " + z + ", " + yaw + ")";
			SQLUtil.transactUpdate(update);
			if (caller.getName()!=playerName) {
				caller.sendMessage(ChatColor.YELLOW + "Home location for " + ChatColor.WHITE + playerName + ChatColor.YELLOW +
						" in " + ChatColor.WHITE + caller.getWorld().getName() + ChatColor.YELLOW + " set.");
			} else caller.sendMessage(ChatColor.YELLOW + "Home location in " + ChatColor.WHITE + caller.getWorld().getName() + 
					ChatColor.YELLOW + " set.");
			return true;
		}
	}
	

}
