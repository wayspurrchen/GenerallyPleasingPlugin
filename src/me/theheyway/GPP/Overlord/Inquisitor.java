package me.theheyway.GPP.Overlord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.StringTokenizer;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Util.CommandUtil;
import me.theheyway.GPP.Util.GenUtil;
import me.theheyway.GPP.Util.TypeUtil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Inquisitor implements CommandExecutor {
	
	private GPP plugin;
	private Overlord overlord;
	
	public Inquisitor(GPP plugin, Overlord overlord) {
		this.plugin = plugin;
		this.overlord = overlord;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		Player player = (Player) sender;
		
		//Commands for GPP base
		if (CommandUtil.cmdEquals(command, "clear")) {
			if (args.length > 0) {
				if (player.hasPermission("gpp.overlord.clearother")) {
					Player target = GenUtil.getPlayerMatch(args[0]);
					if (target!=null) {
						Inventory inv = target.getInventory();
						inv.clear();
						player.sendMessage(ChatColor.YELLOW + "You cleared " + ChatColor.WHITE + target.getName() + ChatColor.YELLOW + "'s inventory.");
						return true;
					} else {
						player.sendMessage(ChatColor.RED + "Could not find player.");
						return true;
					}
				} else {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permissions to do that.");
					return true;
				}
			} else {
				Inventory inv = player.getInventory();
				inv.clear();
				player.sendMessage(ChatColor.YELLOW + "You cleared your inventory.");
				return true;
			}
		} else if (CommandUtil.cmdEquals(command, "strip")) {
			if (args.length > 0) {
				if (args.length > 1) {
					Player target = GenUtil.getPlayerMatch(args[0]);
					if (target!=null) {
						Inventory targetInv = target.getInventory();
						ArrayList<String> unremovables = new ArrayList<String>();
						String[] strings = args[1].split(",");
						ArrayList<ItemStack> items;
						for (int i=0; i < strings.length; i++) {
							if (Material.matchMaterial(strings[i]) != null) {
								Material mat = Material.matchMaterial(strings[i]);
								targetInv.remove(mat);
							} else if (TypeUtil.isInteger(strings[i])) {
								if (Material.getMaterial(Integer.parseInt(strings[i])) != null)	targetInv.remove(Integer.parseInt(strings[i]));
							} else unremovables.add(strings[i]);
						}
						String errorString = "Could not remove items [";
						boolean errorsFound = false;
						for (Iterator<String> unIter = unremovables.iterator(); unIter.hasNext();) {
							errorsFound = true;
							errorString += unIter.next();
							if (unIter.hasNext()) errorString += ",";
						}
						errorString += "].";
						if (errorsFound) player.sendMessage(ChatColor.RED + errorString);
						else player.sendMessage(ChatColor.YELLOW + "You stripped " + ChatColor.WHITE + target.getName() + ChatColor.YELLOW + ".");
						return true;
					} else {
						player.sendMessage(ChatColor.RED + "Could not find player.");
						return true;
					}
				} else {
					Inventory targetInv = player.getInventory();
					ArrayList<String> unremovables = new ArrayList<String>();
					String[] strings = args[0].split(",");
					for (int i=0; i < strings.length; i++) {
						if (Material.matchMaterial(strings[i]) != null) {
							Material mat = Material.matchMaterial(strings[i]);
							targetInv.remove(mat);
						} else if (TypeUtil.isInteger(strings[i])) {
							if (Material.getMaterial(Integer.parseInt(strings[i])) != null)	targetInv.remove(Integer.parseInt(strings[i]));
						} else unremovables.add(strings[i]);
					}
					String errorString = "Could not remove items [";
					boolean errorsFound = false;
					for (Iterator<String> unIter = unremovables.iterator(); unIter.hasNext();) {
						errorsFound = true;
						errorString += unIter.next() + ",";
					}
					errorString += "].";
					if (errorsFound) player.sendMessage(ChatColor.RED + errorString);
					else player.sendMessage(ChatColor.YELLOW + "You stripped yourself.");
					return true;
					
				}
			} return false;
		}
		
		return false;
			
	}

}
