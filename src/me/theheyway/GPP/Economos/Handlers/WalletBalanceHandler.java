package me.theheyway.GPP.Economos.Handlers;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.theheyway.GPP.Economos.AccountUtil;
import me.theheyway.GPP.Economos.Executors.WalletExecutor;
import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Exceptions.NoPermissionException;
import me.theheyway.GPP.Exceptions.NoPlayerException;
import me.theheyway.GPP.Exceptions.NotIntegerException;
import me.theheyway.GPP.Util.Arguments;

public class WalletBalanceHandler {

	public static boolean direct(Player executor, Arguments args) throws GPPException, SQLException {
		//If there is at least one argument and it is "top", pass command to WalletDisplayTop Handler
		if (args.size()>=1 && args.getString(0).equals("top")) {
			if (executor.hasPermission("gpp.economos.wallet.top")) {
				if (args.size()==2) {
					if (args.isInt(1)) {
						WalletExecutor.displayWealthiest(executor, args.getInt(1));
						return true;
					} else {
						throw new NotIntegerException();
					}
				} else {
					WalletExecutor.displayWealthiest(executor, 5);
					return true;
				}
			} else {
				throw new NoPermissionException();
			}
		//Otherwise, run the /bal command to show balance by whatever args specified
		} else {
			if (args.size() >= 1) {
				String targetName = args.getPlayerNameMatch(0);
				if (targetName!=null) {
					if (AccountUtil.guaranteeAccountant(targetName) != null) {
						WalletExecutor.displayWalletBalance(executor, targetName);
						return true;
					} else {
						throw new GPPException(ChatColor.DARK_RED + "Player does not have a wallet yet.");
					}
				} else {
					targetName = args.getString(0);
					if (AccountUtil.walletExists(targetName)) {
						WalletExecutor.displayWalletBalance(executor, targetName);
						return true;
					} else {
						throw new NoPlayerException();
					}
				}
			} else {
				if (AccountUtil.guaranteeAccountant(executor.getName()) != null) {
					WalletExecutor.displayWalletBalance(executor, executor.getName());
					return true;
				} else {
					throw new GPPException(ChatColor.DARK_RED + "You don't have a wallet. This isn't right. Please reconnect and inform theheyway.");
				}
			}
		}
	}
	
	
}
