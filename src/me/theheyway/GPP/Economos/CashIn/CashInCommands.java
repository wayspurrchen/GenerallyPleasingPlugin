package me.theheyway.GPP.Economos.CashIn;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Economos.EconomosConstants;
import me.theheyway.GPP.Economos.Economos;
import me.theheyway.GPP.Util.CommandUtil;
import me.theheyway.GPP.Util.TypeUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CashInCommands implements CommandExecutor {
	
	private static GPP plugin;
	private static Economos economos;
	
	public CashInCommands(GPP plugin, Economos economos) {
		CashInCommands.plugin = plugin;
		CashInCommands.economos = economos;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		Player player = (Player) sender;
	
		if (EconomosConstants.CASHIN_ENABLED) {
			if (CommandUtil.cmdEquals(command, "level")) {
				CashInCommands.displayLevel(player);
				return true;
			} else if (CommandUtil.cmdEquals(command, "cashin")) {
				if (args.length > 0) {
					CashInCommands.cashIn(player, args);
					return true;
				} else return false;
			} else if (CommandUtil.cmdEquals(command, "exchangerate")) {
				CashInCommands.checkExchange(player, args);
				return true;
			} else if (CommandUtil.cmdEquals(command, "setlevel")) {
				if (args.length > 0) {
					CashInCommands.setLevel(player, args);
					return true;
				} else return false;
			
			
		} else player.sendMessage(ChatColor.GRAY + "Economos CashIn module is not enabled on this server.");
		
		return true;
		}
		return true;
	}
	
	public static void cashIn(Player player, String[] args) {
		if (TypeUtil.isInteger(args[0])) {
			if (player.getTotalExperience() > 0) {
				if (player.getTotalExperience() >= Integer.parseInt(args[0])) {
					Double exchangeRate = CashInConstants.CASH_PER_LEVEL;
					Double cash = Double.valueOf(args[0])*exchangeRate;
					economos.accMan.incrementWalletBalance(player.getName(), cash);
					player.setTotalExperience(player.getTotalExperience()-Integer.valueOf(args[0]));
					player.sendMessage(ChatColor.YELLOW + "You exchanged " + args[0] + " levels for " + cash + ".");
					player.sendMessage(ChatColor.YELLOW + "Exchange rate: " + ChatColor.WHITE + exchangeRate);
				}
			} else player.sendMessage(ChatColor.RED + "You have no levels to cash in.");
		} else player.sendMessage(ChatColor.RED + "/cashin requires an integer.");
	}
	
	public static void checkExchange(Player player, String[] args) {
		player.sendMessage(ChatColor.YELLOW + "CashIn exchange rate: " + CashInConstants.CASH_PER_LEVEL);
	}

	
	
}
