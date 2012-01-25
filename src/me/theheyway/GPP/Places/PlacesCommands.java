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
import me.theheyway.GPP.Overlord.Cruelty;
import me.theheyway.GPP.Overlord.Overlord;
import me.theheyway.GPP.Util.CommandUtil;
import me.theheyway.GPP.Util.PlaceUtil;
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
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		Player player = (Player) sender;
		
		//Commands for Ports
		if (CommandUtil.cmdEquals(command, "home")) {
			if (!Cruelty.isFrozen(player)) {
				if (args.length==1) {
					String target = GenUtil.getPlayerMatchString(args[0]);
					if (PlaceUtil.placeExists("home", "home", target, player.getWorld().getName())) {
						double coords[] = PlaceUtil.getPlace("home", "home", target, player.getWorld().getName());
						Location loc = new Location(player.getWorld(), coords[0], coords[1], coords[2]);
						loc.setYaw((float) coords[3]);
						player.teleport(loc);
						player.sendMessage(ChatColor.YELLOW + "Teleported to " + ChatColor.WHITE + target + ChatColor.YELLOW
								+ "'s home.");
						return true;
					} else {
						player.sendMessage(ChatColor.DARK_RED + "Player " + ChatColor.RED + args[0] + ChatColor.DARK_RED + " does not have a home.");
						return true;
					}
				} else if (PlaceUtil.placeExists("home", "home", player.getName(), player.getWorld().getName())) {
					double coords[] = PlaceUtil.getPlace("home", "home", player.getName(), player.getWorld().getName());
					
					Location loc = new Location(player.getWorld(), coords[0], coords[1], coords[2]);
					loc.setYaw((float) coords[3]);
					player.teleport(loc);
					return true;
				} else {
					player.sendMessage(ChatColor.RED + "You have not set a home on this world. Use /sethome to do so.");
					return true;
				}
			} else {
				player.sendMessage(ChatColor.RED + "You are frozen.");
			}
		} else if (CommandUtil.cmdEquals(command, "jumpto")) {
			if (!Cruelty.isFrozen(player)) {
				teleportToBlock(player, player);
				return true;
			} else {
				player.sendMessage(ChatColor.RED + "You have not set a home on this world. Use /sethome to do so.");
				return true;
			}
		} else if (CommandUtil.cmdEquals(command, "put")) {
			if (args.length==1) {
				Player target = GenUtil.getPlayerMatch(args[0]);
				if (target!=null) {
					teleportToBlock(player, target);
					player.sendMessage(ChatColor.YELLOW + "Put " + ChatColor.WHITE + target.getName() + ChatColor.YELLOW + " to target.");
					return true;
				} else {
					player.sendMessage(ChatColor.DARK_RED + "Could not find player.");
					return true;
				}
			} else {
				player.sendMessage(ChatColor.DARK_RED + "Could not find player.");
				return true;
			}
		} else if (CommandUtil.cmdEquals(command, "sethome")) {
			try {
				if (args.length==0) {
					if (setHome(player, player.getName())) return true;
				} else {
					if (PlaceUtil.placeExists("home", "home", args[0], player.getWorld().getName())) {
						if (setHome(player, args[0])) return true;
					} else {
						Player subject = GenUtil.getPlayerMatch(args[0]);
						if (subject!=null) {
							setHome(player, subject.getName());
							return true;
						} else {
							player.sendMessage(ChatColor.DARK_RED + "Could not find player.");
							return true;
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (CommandUtil.cmdEquals(command, "setspawn")) { //Set save yaw
			Location loc = player.getLocation();
			int x = (int) loc.getX();
			int y = (int) loc.getY();
			int z = (int) loc.getZ();
			double yaw = loc.getYaw();
			player.getWorld().setSpawnLocation(x, y, z);
			FileConfiguration spawnYaws = getSpawnYaws();
			spawnYaws.set(player.getWorld().getName(), yaw);
			saveSpawnYaws();
			player.sendMessage(ChatColor.YELLOW + "Spawn for " + ChatColor.WHITE + player.getWorld().getName()
					+ ChatColor.YELLOW + " updated.");
			return true;
		} else if (CommandUtil.cmdEquals(command, "spawn")) {
			if (!Cruelty.isFrozen(player)) {
				FileConfiguration spawnYaws = getSpawnYaws();
				Location loc = player.getWorld().getSpawnLocation();
				loc.setYaw((float) spawnYaws.getDouble(player.getWorld().getName(), 0.0));
				player.teleport(loc);
				return true;
			} else {
				player.sendMessage(ChatColor.RED + "You are frozen.");
			}
		} else if (CommandUtil.cmdEquals(command, "summon")) {
			if (args.length > 1) {
				if (args[0]!="all") {
					Player target = GenUtil.getPlayerMatch(args[0]);
					if (target!=null) {
						target.teleport(player);
						player.sendMessage(ChatColor.YELLOW + "Teleported " + ChatColor.WHITE + target.getName() + ChatColor.YELLOW + " to you.");
						target.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.YELLOW + " has summoned you.");
					} else {
						player.sendMessage(ChatColor.DARK_RED + "Could not find player.");
						return true;
					}
				} else {
					Player[] all = Bukkit.getServer().getOnlinePlayers();
					for (int i = 0; i < all.length; i++) {
						if (all[i]!=player) {
							all[i].teleport(player);
							all[i].sendMessage(ChatColor.WHITE + player.getName() + ChatColor.YELLOW + " has summoned you.");
						}
					}
				}
			} else return false;
		} else if (CommandUtil.cmdEquals(command, "tp")) {
			if (!Cruelty.isFrozen(player)) {
				if (args.length>0) {
					if (args.length==1) {
						Player target = GenUtil.getPlayerMatch(args[0]);
						if (target!=null) {
							if (target==player) {
								player.sendMessage(ChatColor.YELLOW + "You wiggle around a bit.");
								return true;
							} else {
								player.teleport(target);
								player.sendMessage(ChatColor.YELLOW + "Teleported to " + ChatColor.WHITE + target.getName() + ChatColor.YELLOW + ".");
								return true;
							}
						} else {
							player.sendMessage(ChatColor.DARK_RED + "Could not find player.");
							return true;
						}
					} else if (args.length>=2) {
						if (player.hasPermission("gpp.ports.tpother")) {
							Player target1 = GenUtil.getPlayerMatch(args[0]);
							Player target2 = GenUtil.getPlayerMatch(args[1]);
							if (target1 != null && target2 != null) {
								if (target1 == target2) {
									if (target1 == player) {
										player.sendMessage(ChatColor.YELLOW + "Redundancy at its finest.");
										return true;
									} else {
										player.sendMessage(ChatColor.YELLOW + "You make the person wiggle.");
										target1.sendMessage("You wiggle around a bit.");
										return true;
									}
								} else {
									target1.teleport(target2);
									player.sendMessage(ChatColor.YELLOW + "You teleported " + ChatColor.WHITE + target1.getName() + ChatColor.YELLOW + 
											" to " + ChatColor.WHITE + target2.getName() + ChatColor.YELLOW + ". ");
									return true;
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "Could not find one or both players.");
								return true;
							}
						}
					}
				} else return false;
			} else {
				player.sendMessage(ChatColor.RED + "You are frozen.");
			}
		}
		
		return false;
			
	}
	
	/*
	public boolean locationExists(String type, String name, String owner, String world) {
		String query = "SELECT * FROM " + DB_LOCATIONS_TABLENAME +
				" WHERE type='" + type + "' AND name='" + name + "' AND owner='" + owner + "' AND world='" + world + "'";
		try {
			Connection conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(query);
			boolean exists = false;
			if (rs.next()) exists = true;
			rs.close();
			conn.close();
			return exists;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	*/
	
	/*
	public double[] getLocation(String type, String name, String owner, String world) {
		String query = "SELECT x,y,z,yaw FROM " + DB_LOCATIONS_TABLENAME +
				" WHERE type='" + type + "' AND name='" + name + "' AND owner='" + owner + "' AND world='" + world + "'";
		try {
			Connection conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(query);
			rs.next();
			double xyzyaw[] = new double[4];
			xyzyaw[0] = rs.getDouble(1);
			xyzyaw[1] = rs.getDouble(2);
			xyzyaw[2] = rs.getDouble(3);
			xyzyaw[3] = rs.getDouble(4);
			rs.close();
			conn.close();
			return xyzyaw;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
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
