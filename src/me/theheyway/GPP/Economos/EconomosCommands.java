package me.theheyway.GPP.Economos;

import java.sql.SQLException;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Economos.EconomosConstants;
import me.theheyway.GPP.Economos.Handlers.AccountHandler;
import me.theheyway.GPP.Economos.Handlers.WalletBalanceHandler;
import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Util.Arguments;
import me.theheyway.GPP.Util.CommandUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomosCommands implements CommandExecutor {
	
	static GPP plugin;
	static Economos economos;
	
	EconomosCommands(GPP plugin, Economos economos) {
		this.plugin = plugin;
		this.economos = economos;
	}
	
	//This method is so long. So, so, very long.
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias,
			String[] arguments) {
		Player executor = (Player) sender;
		String executorName = executor.getName();
		Arguments args = new Arguments(arguments);

		//Balance command
		if (CommandUtil.cmdEquals(command, "balance")) {
			try {
				if (WalletBalanceHandler.direct(executor, args)) return true;
			} catch (GPPException e) {
				executor.sendMessage(e.getMessage());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		//Account command
		} else if (CommandUtil.cmdEquals(command, "account")) {
			try {
				if (AccountHandler.direct(executor, args)) return true;
				else return false;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (GPPException e) {
				executor.sendMessage(e.getMessage());
			}
			
		}
		
		return true;
	}
	

}
